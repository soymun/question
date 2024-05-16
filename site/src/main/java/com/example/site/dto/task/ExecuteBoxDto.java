package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Выполнение выбора")
public class ExecuteBoxDto {

    @Schema(description = "Задача")
    private Long taskId;

    @Schema(description = "Выбранные ответы")
    private List<Long> resultIds;
}
