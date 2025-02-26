package com.example.site.dto.usercourse;

import com.example.site.dto.task.UserTaskDto;
import lombok.Data;

import java.util.List;

@Data
public class UserCourseWithUserTaskDto {

    private UserCourseDto userCourse;

    private List<UserTaskDto> userTasks;
}
