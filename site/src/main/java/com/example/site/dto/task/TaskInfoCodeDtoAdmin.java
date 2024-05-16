package com.example.site.dto.task;

import com.example.site.model.CodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Информация о коде")
public class TaskInfoCodeDtoAdmin {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Задача")
    private Long task;

    @Schema(description = "Тип")
    private CodeType codeType;

    @Schema(description = "Код")
    private String initCode;

    @Schema(description = "Проверка")
    private String checkCode;

    @Schema(description = "Класс проверки")
    private String checkClass;

    @Schema(description = "Класс пользовательского")
    private String userClass;
}
