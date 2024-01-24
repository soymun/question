package com.example.site.dto.user_course;

import lombok.Data;

@Data
public class UserCourseCreateDto {

    private Long userId;

    private Long courseId;
}
