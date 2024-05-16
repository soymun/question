package com.example.site.dto.usercourse;

import com.example.site.dto.user.UserInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Пользовательские курсу")
public class UserCourseDto {

    @Schema(description = "Пользователь")
    private UserInclude userCourseId;

    @Schema(description = "Оценка")
    private MarkIncludeDto courseMarks;

    @Schema(description = "Начало")
    private LocalDateTime startDate;

    @Schema(description = "Закрыт")
    private Boolean closed;
}
