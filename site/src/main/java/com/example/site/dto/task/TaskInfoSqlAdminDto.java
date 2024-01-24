package com.example.site.dto.task;

import lombok.Data;

@Data
public class TaskInfoSqlAdminDto {

    private Long id;

    private Long task;

    private String checkSql;

    private String mainSql;
}
