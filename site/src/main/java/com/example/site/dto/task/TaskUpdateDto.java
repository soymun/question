package com.example.site.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Задача")
public class TaskUpdateDto {

    @NotNull
    @Schema(description = "ID")
    private Long id;

    @NotNull
    @Schema(description = "Номер")
    private Long number;

    @Schema(description = "Название")
    private String name;

    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Заголовок")
    private String title;

    @Schema(description = "Открыта")
    private Boolean open;
}
