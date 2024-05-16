package com.example.site.dto.group;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Группа")
public class GroupCreateDto {

    @NotNull
    @Schema(description = "Полное название")
    private String fullName;

    @Schema(description = "Сокращённое название")
    private String shortName;
}
