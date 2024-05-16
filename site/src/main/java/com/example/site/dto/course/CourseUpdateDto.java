package com.example.site.dto.course;

import com.example.site.model.CourseType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Курс")
public class CourseUpdateDto {

    @NotNull
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "Название")
    private String courseName;

    @Schema(description = "Описание")
    private String about;

    @Schema(description = "Лого")
    private String pathImage;

    @Schema(description = "Тип курса")
    private CourseType courseType;

    @Schema(description = "Открытый/закрытый")
    private Boolean open;

    @Schema(description = "Время выполнения")
    private LocalDateTime timeExecute;
}
