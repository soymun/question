package com.example.site.dto.usercourse;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Присоединение пользователя")
public class UserCourseCreateDto {

    @NotNull
    @Schema(description = "Пользователь")
    private Long userId;

    @NotNull
    @Schema(description = "Курс")
    private Long courseId;
}
