package com.example.executesqlscriptsmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeExecuteResponse {

    private Status status;

    private String message;

    private Long userId;

    private Long taskId;

    private Long time;

    private Long attempt;
}
