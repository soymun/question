package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Информация о выборе")
public class TaskInfoQuestionBoxUpdateDto {

    @NotNull
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Ответ")
    private String answer;

    @Schema(description = "Правильность")
    private Boolean rights;
}
