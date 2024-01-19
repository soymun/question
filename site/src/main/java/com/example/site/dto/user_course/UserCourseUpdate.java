package com.example.site.dto.user_course;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserCourseUpdate {

    private Long userId;

    private Long courseId;

    private LocalDateTime startCourse;

    private LocalDateTime endCourse;


}
