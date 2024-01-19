package com.example.site.dto.task;

import lombok.Data;

@Data
public class TaskInfoCodeUpdateDto {

    private Long id;

    private String initCode;

    private String checkCode;

    private String checkClass;

    private String userClass;
}
