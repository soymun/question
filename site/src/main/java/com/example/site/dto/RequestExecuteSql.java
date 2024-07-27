package com.example.site.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestExecuteSql {

    private String schema;

    private String userSql;

    private Long userId;

    private Boolean admin;
}
