package com.example.site.dto.task;

import com.example.site.model.TaskType;
import lombok.Data;

@Data
public class TaskCreateDto {

    private Long number;

    private String name;

    private String description;

    private String title;

    private TaskType taskType;

    private Long courses;

    private Boolean open;
}
