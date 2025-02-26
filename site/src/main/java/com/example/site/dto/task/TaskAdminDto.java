package com.example.site.dto.task;

import com.example.site.model.util.TaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Создание задачи")
public class TaskAdminDto {

    @Schema(description = "ID")
    private Long id;

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

    @NotNull
    private Long taskGroup;

    @Schema(description = "Вложенный файл")
    private String file;

    @Schema(description = "Открытость")
    private Boolean open;

    private List<TaskInfoCodeAdminDto> taskInfoCode;

    private List<TaskInfoQuestionBoxAdminDto> taskInfoQuestionBox;

    private TaskInfoQuestionTextAdminDto taskInfoQuestionText;

    private TaskInfoSqlAdminDto taskInfoSql;
}
