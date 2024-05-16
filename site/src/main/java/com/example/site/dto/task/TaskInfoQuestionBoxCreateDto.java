package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Информация о выборе")
public class TaskInfoQuestionBoxCreateDto {

    @NotNull
    @Schema(description = "Задача")
    private Long task;

    @Schema(description = "Ответ")
    private String answer;

    @Schema(description = "Верность")
    private Boolean rights;
}
