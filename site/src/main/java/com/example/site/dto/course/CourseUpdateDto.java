package com.example.site.dto.course;

import com.example.site.model.CourseType;
import lombok.Data;

@Data
public class CourseUpdateDto {

    private Long id;

    private String courseName;

    private String about;

    private String pathImage;

    private CourseType courseType;
}
