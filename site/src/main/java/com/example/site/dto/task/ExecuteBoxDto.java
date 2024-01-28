package com.example.site.dto.task;

import lombok.Data;

import java.util.List;

@Data
public class ExecuteBoxDto {

    private Long taskId;

    private List<Long> resultIds;
}
