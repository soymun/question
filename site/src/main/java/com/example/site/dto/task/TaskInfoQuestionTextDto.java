package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Информация о тексте")
public class TaskInfoQuestionTextDto {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Задача")
    private Long task;

    @Schema(description = "Ответ")
    private String answer;

}
