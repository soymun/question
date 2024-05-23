package com.example.site.dto.task;

import com.example.site.model.DcCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Информация о коде")
public class TaskInfoCodeCreateDto {

    @NotNull
    @Schema(description = "Задача")
    private Long task;

    @NotNull
    @Schema(description = "Тип кода")
    private DcCodeType codeType;

    @Schema(description = "Код инициализации")
    private String initCode;

    @Schema(description = "Код проверки")
    private String checkCode;

    @Schema(description = "Название класса проверки")
    private String checkClass;

    @Schema(description = "Название пользовательского класса")
    private String userClass;
}
