package com.example.javaexecute.service.impl;

import com.example.javaexecute.dto.*;
import com.example.javaexecute.service.JavaExecuteService;
import lombok.RequiredArgsConstructor;
import org.mdkt.compiler.InMemoryJavaCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JavaExecuteServiceImpl implements JavaExecuteService {

    @Autowired
    private InMemoryJavaCompiler inMemoryJavaCompiler;

    @Override
    public CodeExecuteResponse execute(CodeExecuteRequest executeRequest) {
        String p = "t" + executeRequest.getTaskId() + ".u" + executeRequest.getUserId() + ".a" + executeRequest.getAttempt();

        String pakcage = "package " + p + ";\n";

        try {
            String userCode = pakcage + executeRequest.getUserCode();
            String checkCode = pakcage + executeRequest.getCheckCode();
            inMemoryJavaCompiler.compile(p + "." + executeRequest.getUserClass(), userCode);
            Class<?> checkClass = inMemoryJavaCompiler.compile(p + "." + executeRequest.getCheckClass(), checkCode);

            Date start = new Date();
            checkClass.getMethod("check").invoke(checkClass.newInstance());
            Date end = new Date();
            return CodeExecuteResponse
                    .builder()
                    .message("OK")
                    .status(Status.OK)
                    .userId(executeRequest.getUserId())
                    .taskId(executeRequest.getTaskId())
                    .attempt(executeRequest.getAttempt())
                    .time(end.getTime() - start.getTime())
                    .build();
        } catch (Exception e) {
            return CodeExecuteResponse
                    .builder()
                    .message(e.getMessage())
                    .status(Status.ERROR)
                    .attempt(executeRequest.getAttempt())
                    .userId(executeRequest.getUserId())
                    .taskId(executeRequest.getTaskId())
                    .build();
        }
    }
}
