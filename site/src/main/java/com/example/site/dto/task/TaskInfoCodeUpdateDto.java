package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Информация о коде")
public class TaskInfoCodeUpdateDto {

    @NotNull
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Код")
    private String initCode;

    @Schema(description = "Проверка")
    private String checkCode;

    @Schema(description = "Класс проверки")
    private String checkClass;

    @Schema(description = "Пользовательский класс")
    private String userClass;
}
