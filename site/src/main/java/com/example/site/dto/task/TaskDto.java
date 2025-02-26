package com.example.site.dto.task;

import com.example.site.model.util.TaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Задача")
public class TaskDto {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Номер")
    private Long number;

    @Schema(description = "Название")
    private String name;

    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Заголовок")
    private String title;

    @Schema(description = "Вложенный файл")
    private String file;

    @NotNull
    private Long taskGroup;

    @Schema(description = "Тип")
    private TaskType taskType;

    @Schema(description = "Курс")
    private Long courses;

    @Schema(description = "Удалена")
    private Boolean deleted;

    @Schema(description = "Открыта")
    private Boolean open;

    @Schema(description = "Все выполненные")
    private Long allAttempt;

    @Schema(description = "Правильно выполнены")
    private Long rightAttempt;
}
