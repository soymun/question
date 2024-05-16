package com.example.site.dto.history;

import com.example.site.dto.user.UserInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "История")
public class HistoryDto {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Задача")
    private TaskInclude task;

    @Schema(description = "Пользователь")
    private UserInclude user;

    @Schema(description = "Сообщение")
    private String message;

    @Schema(description = "Верность")
    private Boolean rights;

    @Schema(description = "Код")
    private String code;
}
