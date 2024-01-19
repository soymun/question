package com.example.site.dto.task;

import lombok.Data;

@Data
public class TaskUpdateDto {

    private Long id;

    private Long number;

    private String name;

    private String description;

    private String title;

    private Boolean open;
}
