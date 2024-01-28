package com.example.site.dto.task;

import lombok.Data;

@Data
public class TaskInfoSqlCreateDto {

    private Long task;

    private String checkSql;

    private String mainSql;
}
