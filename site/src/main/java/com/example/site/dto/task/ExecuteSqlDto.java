package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Выполнение sql")
public class ExecuteSqlDto {

    @Schema(description = "Код пользователя")
    private String userSql;
}
