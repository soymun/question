package com.example.site.service;

import com.example.site.dto.user_course.UserCourseDto;
import com.example.site.dto.user_course.UserCourseUpdate;

import java.util.List;

public interface UserCourseService {

    void saveUserCourse(Long userId, Long courseId, Long thisUser, boolean admin);

    void updateUserCourse(UserCourseUpdate userCourseUpdate);

    void deleteCourse(Long userId, Long courseId, Long thisUser, boolean admin);

    void deleteUserCoursesByGroupId(Long courseId, Long groupId, Long thisUser, boolean admin);

    List<UserCourseDto> getUserByCourseIdAndGroupId(Long courseId, Long groupId, Long thisUser, boolean admin);

    UserCourseDto getUserCourseByUserIdAndCourseId(Long courseId, Long userId);
}
