package com.example.site.dto.course;

import com.example.site.model.util.CourseType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Создание курса")
public class CourseCreateDto {

    @NotNull
    @Schema(description = "Название")
    private String courseName;

    @Schema(description = "Описание")
    private String about;

    @Schema(description = "Лого")
    private String pathImage;

    @NotNull
    @Schema(description = "Тип курса")
    private CourseType courseType;

    @NotNull
    @Schema(description = "Создатель курса")
    private Long userCreated;

    @Schema(description = "Открытый/закрытый")
    private Boolean open;

    @Schema(description = "Время выполнения")
    private LocalDateTime timeExecute;
}
