package com.example.site.dto.task;

import lombok.Data;

@Data
public class ExecuteSqlDto {

    private Long id;

    private Long courseId;

    private Long userId;

    private String userSql;
}
