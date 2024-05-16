package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Информация о выборе")
public class TaskInfoQuestionBoxAdminDto {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Задача")
    private Long task;

    @Schema(description = "Ответ")
    private String answer;

    @Schema(description = "Верность")
    private Boolean rights;
}
