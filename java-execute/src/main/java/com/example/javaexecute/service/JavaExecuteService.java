package com.example.javaexecute.service;


import com.example.javaexecute.dto.*;

public interface JavaExecuteService {

    CodeExecuteResponse execute(CodeExecuteRequest executeRequest);
}
