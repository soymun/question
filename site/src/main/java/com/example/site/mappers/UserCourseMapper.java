package com.example.site.mappers;

import com.example.site.dto.task.UserTaskDto;
import com.example.site.dto.usercourse.UserCourseDto;
import com.example.site.model.Courses;
import com.example.site.model.TaskGroup;
import com.example.site.model.UserCourse;
import com.example.site.model.UserTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserCourseMapper {

    @Mapping(source = "userCourseId.user", target = "user")
    @Mapping(source = "userCourseId.courses", target = "course")
    UserCourseDto userCourseToUserCourseDto(UserCourse userCourse);

    @Mapping(source = "userTaskId.task", target = "userTaskId")
    UserTaskDto userCourseToUserTaskDto(UserTask userTask);

    default Long map(TaskGroup value) {
        return value.getId();
    }

    default Long map1(Courses value) {
        return value.getId();
    }
}
