package com.example.site.mappers;

import com.example.site.dto.user_course.UserCourseDto;
import com.example.site.model.UserCourse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCourseMapper {

    UserCourseDto userCourseToUserCourseDto(UserCourse userCourse);
}
