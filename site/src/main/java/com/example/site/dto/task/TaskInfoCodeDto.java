package com.example.site.dto.task;

import com.example.site.dto.dccodetype.DcCodeTypeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Информация о коде")
public class TaskInfoCodeDto {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Тип")
    private DcCodeTypeDto codeType;

    @Schema(description = "Код")
    private String initCode;
}
