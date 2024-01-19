package com.example.site.dto.user_course;

import com.example.site.dto.marks.MarkDto;
import com.example.site.dto.user.UserInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserCourseDto {

    private UserInclude userCourseId;

    private MarkDto courseMarks;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean closed;
}
