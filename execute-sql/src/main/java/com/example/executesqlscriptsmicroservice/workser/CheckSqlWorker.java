package com.example.executesqlscriptsmicroservice.workser;

import com.example.executesqlscriptsmicroservice.service.impl.QueryExecuteServiceImpl;
import dto.CodeExecuteRequest;
import dto.RequestCheckSql;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CheckSqlWorker {

    @Autowired
    private QueryExecuteServiceImpl queryExecuteService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "check", group = "checkers")
    public void checkSql(RequestCheckSql requestCheckSql){
        if(requestCheckSql != null) {
            String lowerSql = requestCheckSql.getUserSql().toLowerCase();
            if (!lowerSql.contains("insert")
                    && !lowerSql.contains("delete")
                    && !lowerSql.contains("update")
                    && !lowerSql.contains("drop")
                    && !lowerSql.contains("create")
                    && !lowerSql.contains("alter")) {
                rabbitTemplate.convertAndSend("result", queryExecuteService.checkSelectSql(requestCheckSql));
            } else {
                rabbitTemplate.convertAndSend("result", queryExecuteService.checkSql(requestCheckSql));
            }
        }
    }
}
