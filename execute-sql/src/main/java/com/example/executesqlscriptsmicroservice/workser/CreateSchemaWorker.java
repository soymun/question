package com.example.executesqlscriptsmicroservice.workser;

import com.example.executesqlscriptsmicroservice.service.impl.QueryExecuteServiceImpl;
import dto.RequestCreateSchema;
import dto.ResponseCreateSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateSchemaWorker {

    private final QueryExecuteServiceImpl queryExecuteService;

    @RabbitListener(queues = "schema", group = "schemas")
    public ResponseCreateSchema createSchema(RequestCreateSchema requestCreateSchema){
        if(requestCreateSchema != null){
            log.info("Create schema {}", requestCreateSchema.getCourseId());
            return queryExecuteService.createSchema(requestCreateSchema);
        }
        return ResponseCreateSchema.builder().build();
    }
}
