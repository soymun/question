package com.example.site.dto.sandbox;

import lombok.Data;

@Data
public class SandboxDto {

    private Integer id;

    private String schemaName;

    private Boolean open;

    private String sqlClear;
}
