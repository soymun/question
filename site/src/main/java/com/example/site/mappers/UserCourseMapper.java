package com.example.site.mappers;

import com.example.site.dto.usercourse.UserCourseDto;
import com.example.site.model.UserCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserCourseMapper {

    @Mapping(source = "userCourseId.user", target = "user")
    @Mapping(source = "userCourseId.courses", target = "course")
    UserCourseDto userCourseToUserCourseDto(UserCourse userCourse);
}
