package com.example.site.dto.task;

import lombok.Data;

@Data
public class TaskInfoSqlUpdateDto {

    private Long id;

    private String checkSql;

    private String mainSql;
}
