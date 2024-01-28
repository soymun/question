package com.example.site.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserTaskDto {

    private TaskDto userTaskId;

    private Boolean rights;

    private Boolean closed;

    private Long attempt;
}
