package com.example.site.dto.course;

import lombok.Data;

@Data
public class ExecuteSqlDto {

    private Long courseId;

    private String userSql;
}
