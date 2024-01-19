package com.example.site.dto.task;

import lombok.Data;

@Data
public class TaskInfoQuestionBoxCreateDto {

    private Long task;

    private String answer;

    private Boolean rights;
}
