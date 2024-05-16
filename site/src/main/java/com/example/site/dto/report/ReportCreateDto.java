package com.example.site.dto.report;

import lombok.Data;

@Data
public class ReportCreateDto {

    private String name;

    private String fileName;

    private String sql;
}
