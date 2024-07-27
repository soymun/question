package com.example.javaexecute.workers;

import com.example.javaexecute.dto.*;
import com.example.javaexecute.service.impl.JavaExecuteServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JavaExecuteWorker {

    private final JavaExecuteServiceImpl javaExecuteService;

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "JAVA", group = "executors")
    public void execute(CodeExecuteRequest request) {
        if (request != null) {
            log.info("Execute sql user {}", request.getUserId());
            CodeExecuteResponse codeExecuteResponse;
            if (!request.getCheckCode().contains("System.exit") && !request.getUserCode().contains("System.exit")) {
                codeExecuteResponse = javaExecuteService.execute(request);
            } else {
                codeExecuteResponse = CodeExecuteResponse
                        .builder()
                        .message("Bad request")
                        .status(Status.ERROR)
                        .attempt(request.getAttempt())
                        .userId(request.getUserId())
                        .taskId(request.getTaskId())
                        .build();
            }

            rabbitTemplate.convertAndSend("completed-code", codeExecuteResponse);
        }
    }
}
