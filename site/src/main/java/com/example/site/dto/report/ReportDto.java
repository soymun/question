package com.example.site.dto.report;

import lombok.Data;

@Data
public class ReportDto {

    private Long id;

    private String name;

    private String fileName;

    private String sql;
}
