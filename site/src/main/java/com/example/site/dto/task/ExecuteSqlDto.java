package com.example.site.dto.task;

import lombok.Data;

@Data
public class ExecuteSqlDto {

    private Long taskId;

    private String userSql;
}
