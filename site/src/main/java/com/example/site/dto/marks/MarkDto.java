package com.example.site.dto.marks;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Schema(description = "Оценка")
public class MarkDto {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Курс")
    private Long courses;

    @Schema(description = "Количество задач")
    private Long countTask;

    @Schema(description = "Оценка")
    private Long mark;

    @Schema(description = "Дата создания")
    private LocalDateTime create;

    @Schema(description = "Дата обновления")
    private LocalDateTime update;
}
