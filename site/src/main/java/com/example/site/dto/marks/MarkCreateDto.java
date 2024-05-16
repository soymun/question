package com.example.site.dto.marks;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Оценка")
public class MarkCreateDto {

    @NotNull
    @Schema(description = "Курс")
    private Long courses;

    @NotNull
    @Schema(description = "Количество задач")
    private Long countTask;

    @NotNull
    @Schema(description = "Оценка")
    private Long mark;
}
