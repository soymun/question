package com.example.site.dto.report;

import com.example.site.model.util.Permission;
import com.example.site.model.util.Role;
import lombok.Data;

@Data
public class ReportCreateDto {

    private String name;

    private String fileName;

    private String sql;

    private boolean defaultReport;

    private Permission permission;
}
