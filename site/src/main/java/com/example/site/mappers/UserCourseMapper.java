package com.example.site.mappers;

import com.example.site.dto.user.UserInclude;
import com.example.site.dto.user_course.UserCourseDto;
import com.example.site.model.Courses;
import com.example.site.model.User;
import com.example.site.model.UserCourse;
import com.example.site.model.UserCourseId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCourseMapper {

    UserCourseDto userCourseToUserCourseDto(UserCourse userCourse);

    default Long map(Courses courses){
        if(courses == null) return null;
        return courses.getId();
    }

    default UserInclude map1(UserCourseId userCourseId){
        if(userCourseId == null) return null;

        User user = userCourseId.getUser();
        if(user == null) return null;

        UserInclude userInclude = new UserInclude();
        userInclude.setId(user.getId());
        userInclude.setSecondName(user.getSecondName());
        userInclude.setPatronymic(user.getPatronymic());
        userInclude.setFirstName(user.getFirstName());

        return userInclude;
    }
}
