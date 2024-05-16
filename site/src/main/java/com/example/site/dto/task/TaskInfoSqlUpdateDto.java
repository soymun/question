package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Информация о sql")
public class TaskInfoSqlUpdateDto {

    @NotNull
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Проверочный sql")
    private String checkSql;

    @Schema(description = "SQL выполнения")
    private String mainSql;
}
