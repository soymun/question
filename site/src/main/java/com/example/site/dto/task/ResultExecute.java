package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Результат выполнения")
public class ResultExecute {

    @Schema(description = "ID результата")
    private Long id;

    @Schema(description = "ID задачи")
    private Long task;

    @Schema(description = "Сообщение")
    private String message;

    @Schema(description = "Правильность")
    private Boolean rights;

    @Schema(description = "Код")
    private String code;
}
