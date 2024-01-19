package com.example.site.mappers;

import com.example.site.dto.course.CourseCreateDto;
import com.example.site.dto.course.CourseDto;
import com.example.site.model.Courses;
import com.example.site.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    Courses courseCreateDtoToCourse(CourseCreateDto courseCreateDto);

    CourseDto courseToCourseDto(Courses courses);

    default User map1(Long id){
        return new User(id);
    }
}
