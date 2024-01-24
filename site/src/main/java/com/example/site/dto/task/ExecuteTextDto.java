package com.example.site.dto.task;

import lombok.Data;

@Data
public class ExecuteTextDto {

    private Long taskId;

    private Long userId;

    private String text;
}
