package com.example.site.dto.task;

import com.example.site.model.CodeType;
import lombok.Data;

@Data
public class TaskInfoCodeCreateDto {

    private Long task;

    private CodeType codeType;

    private String initCode;

    private String checkCode;

    private String checkClass;

    private String userClass;
}
