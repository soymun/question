package com.example.site.dto.task;

import com.example.site.model.util.TaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TaskUserDto {

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

    @Schema(description = "Вложенный файл")
    private String file;

    @NotNull
    private Long taskGroup;

    @NotNull
    @Schema(description = "Тип задачи")
    private TaskType taskType;

    @NotNull
    @Schema(description = "ID курса")
    private Long courses;

    @Schema(description = "Открытость")
    private Boolean open;

    private List<TaskInfoCodeDto> taskInfoCode;

    private List<TaskInfoQuestionBoxDto> taskInfoQuestionBox;

    private TaskInfoQuestionTextDto taskInfoQuestionText;
}
