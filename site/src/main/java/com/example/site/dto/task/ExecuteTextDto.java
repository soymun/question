package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Выполнение кода")
public class ExecuteTextDto {

    @Schema(description = "Задача")
    private Long taskId;

    @Schema(description = "Ответ пользователя")
    private String answer;
}
