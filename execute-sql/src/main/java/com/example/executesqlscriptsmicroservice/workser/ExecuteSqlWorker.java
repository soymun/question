package com.example.executesqlscriptsmicroservice.workser;

import com.example.executesqlscriptsmicroservice.chain.Context;
import com.example.executesqlscriptsmicroservice.chain.UtilProcess;
import com.example.executesqlscriptsmicroservice.dto.RequestExecuteSql;
import com.example.executesqlscriptsmicroservice.dto.ResponseExecuteSql;
import com.example.executesqlscriptsmicroservice.service.impl.QueryExecuteServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExecuteSqlWorker {

    private final QueryExecuteServiceImpl queryExecuteService;
    private final UtilProcess utilProcess;

    @RabbitListener(queues = "${sql.queue.execute}", group = "executors")
    public List<ResponseExecuteSql> execute(RequestExecuteSql requestExecuteSql) {
        if (requestExecuteSql != null) {
            log.info("Execute sql user {}", requestExecuteSql.getUserId());
            if (requestExecuteSql.getSchema() != null) {
                Context context = utilProcess.process(requestExecuteSql.getUserSql(), 1);

                if (requestExecuteSql.getAdmin()) {
                    return queryExecuteService.executeSqlAdmin(requestExecuteSql);
                } else if (context.verified && requestExecuteSql.getSandBox()) {
                    return queryExecuteService.executeSqlAdmin(requestExecuteSql);
                } else if (!context.verified) {
                    return List.of(ResponseExecuteSql
                            .builder()
                            .message("Sql is not validated.")
                            .userId(requestExecuteSql.getUserId())
                            .build());
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
