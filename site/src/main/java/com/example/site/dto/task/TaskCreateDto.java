package com.example.site.dto.task;

import com.example.site.model.TaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Создание задачи")
public class TaskCreateDto {

    @NotNull
    @Schema(description = "Номер задачи")
    private Long number;

    @Schema(description = "Название задачи")
    private String name;

    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Заголовок")
    private String title;

    @NotNull
    @Schema(description = "Тип задачи")
    private TaskType taskType;

    @NotNull
    @Schema(description = "ID курса")
    private Long courses;

    @Schema(description = "Открытость")
    private Boolean open;
}
