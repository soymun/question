package com.example.site.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeExecuteRequest {

    private Long userId;

    private Long taskId;

    private String checkClass;

    private String userClass;

    private String checkCode;

    private String userCode;

    private Long attempt;
}
