package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExecuteDto {

    @Schema(description = "Задача")
    private Long taskId;

    @Schema(description = "Задача на выбор ответа")
    private ExecuteBoxDto executeBoxDto;

    @Schema(description = "Задача на код")
    private ExecuteCodeDto executeCodeDto;

    @Schema(description = "Задача на sql")
    private ExecuteSqlDto executeSqlDto;

    @Schema(description = "Задача на текст")
    private ExecuteTextDto executeTextDto;
}
