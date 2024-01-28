package com.example.site.dto.course;

import com.example.site.dto.user.UserInclude;
import com.example.site.model.CourseType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseDto {

    private Long id;

    private String courseName;

    private String about;

    private String pathImage;

    private CourseType courseType;

    private UserInclude userCreated;

    private LocalDate timeCreated;

    private Long timeExecute;
}
