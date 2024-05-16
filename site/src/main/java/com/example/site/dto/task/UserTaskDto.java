package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "Задача пользователя")
public class UserTaskDto {

    @Schema(description = "Задача")
    private TaskDto userTaskId;

    @Schema(description = "Правильность")
    private Boolean rights;

    @Schema(description = "Закрыта")
    private Boolean closed;

    @Schema(description = "Попытки")
    private Long attempt;
}
