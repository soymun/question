package com.example.site.dto.task;

import lombok.Data;

@Data
public class TaskInfoQuestionBoxAdminDto {

    private Long id;

    private Long task;

    private String answer;

    private Boolean rights;
}
