package com.example.executesqlscriptsmicroservice.workser;

import dto.*;
import com.example.executesqlscriptsmicroservice.service.impl.QueryExecuteServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExecuteSqlWorker {

    @Autowired
    private QueryExecuteServiceImpl queryExecuteService;

    @RabbitListener(queues = "execute", group = "executors")
    public List<ResponseExecuteSql> execute(RequestExecuteSql requestExecuteSql) {
        if (requestExecuteSql != null) {
            if (requestExecuteSql.getSchema() != null) {
                if (requestExecuteSql.getAdmin()) {
                    return queryExecuteService.executeSqlAdmin(requestExecuteSql);
                } else {
                    return queryExecuteService.executeSqlUser(requestExecuteSql);
                }
            }
            return List.of(ResponseExecuteSql
                    .builder()
                    .message("Схема не указана")
                    .userId(requestExecuteSql.getUserId())
                    .build());
        }
        return List.of(ResponseExecuteSql
                .builder()
                .message("Невозможно выполнить запрос")
                .build());
    }
}
