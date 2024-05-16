package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Информация о sql")
public class TaskInfoSqlAdminDto {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Задача")
    private Long task;

    @Schema(description = "Проверочный sql")
    private String checkSql;

    @Schema(description = "SQL выполнения")
    private String mainSql;
}
