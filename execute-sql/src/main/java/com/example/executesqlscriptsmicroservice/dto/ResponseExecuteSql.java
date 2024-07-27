package com.example.executesqlscriptsmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseExecuteSql {

    private Long userId;

    private List<Map<String, Object>> resultSelect;

    private Integer resultOther;

    private Long time;

    private String message;
}
