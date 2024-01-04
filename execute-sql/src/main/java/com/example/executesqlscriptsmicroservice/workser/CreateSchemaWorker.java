package com.example.executesqlscriptsmicroservice.workser;

import dto.*;
import com.example.executesqlscriptsmicroservice.service.impl.QueryExecuteServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateSchemaWorker {

    @Autowired
    private QueryExecuteServiceImpl queryExecuteService;

    @RabbitListener(queues = "schema", group = "schemas")
    public ResponseCreateSchema createSchema(RequestCreateSchema requestCreateSchema){
        if(requestCreateSchema != null){
            return queryExecuteService.createSchema(requestCreateSchema);
        }
        return ResponseCreateSchema.builder().build();
    }
}
