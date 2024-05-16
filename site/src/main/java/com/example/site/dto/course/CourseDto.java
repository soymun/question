package com.example.site.dto.course;

import com.example.site.dto.user.UserInclude;
import com.example.site.model.CourseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "Курс")
public class CourseDto {

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

    @Schema(description = "Автор")
    private UserInclude userCreated;

    @Schema(description = "Дата создания")
    private LocalDate timeCreated;

    @Schema(description = "Дата закрытия")
    private LocalDateTime timeExecute;
}
