package com.example.site.dto.group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Группа")
public class GroupDto {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Полное название")
    private String fullName;

    @Schema(description = "Сокращённое название")
    private String shortName;
}
