package com.example.executesqlscriptsmicroservice.workser;

import com.example.executesqlscriptsmicroservice.chain.Context;
import com.example.executesqlscriptsmicroservice.chain.UtilProcess;
import com.example.executesqlscriptsmicroservice.dto.*;
import com.example.executesqlscriptsmicroservice.service.impl.QueryExecuteServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CheckSqlWorker {

    private final QueryExecuteServiceImpl queryExecuteService;

    private final RabbitTemplate rabbitTemplate;

    private final UtilProcess utilProcess;

    @RabbitListener(queues = "check", group = "checkers")
    public void checkSql(RequestCheckSql requestCheckSql){
        if(requestCheckSql != null) {
            log.info("Check sql with task {}", requestCheckSql.getTaskUserId());
            Context context = utilProcess.process(requestCheckSql.getUserSql().toLowerCase(), 2);
            if(context.verified) {
                if (context.getSelected()) {
                    rabbitTemplate.convertAndSend("result", queryExecuteService.checkSelectSql(requestCheckSql));
                } else {
                    rabbitTemplate.convertAndSend("result", queryExecuteService.checkSql(requestCheckSql));
                }
            } else {
                rabbitTemplate.convertAndSend("result", ResponseCheckSql
                        .builder()
                                .status(Status.ERROR)
                                .message("Sql is not validated. Delete schemas names.")
                        .build());
            }
        }
    }
}
