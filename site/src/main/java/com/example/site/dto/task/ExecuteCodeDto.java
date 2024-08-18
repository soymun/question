package com.example.site.dto.task;

import com.example.site.dto.dccodetype.DcCodeTypeDto;
import com.example.site.model.DcCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Выполнение кода")
public class ExecuteCodeDto {

    @Schema(description = "Язык")
    private DcCodeTypeDto dcCodeTypeDto;

    @Schema(description = "Код пользователя")
    private String userCode;
}
