package com.example.site.dto.task;

import lombok.Data;

@Data
public class ResultExecute {

    private Long id;

    private Long task;

    private String message;

    private Boolean rights;

    private String code;
}
