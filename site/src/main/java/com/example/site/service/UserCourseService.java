package com.example.site.service;

import com.example.site.dto.usercourse.UserCourseDto;

import java.util.List;

public interface UserCourseService {

    void saveUserCourse(Long userId, Long courseId, Long thisUser, boolean admin);

    void deleteCourse(Long userId, Long courseId, Long thisUser, boolean admin);

    void deleteUserCoursesByGroupId(Long courseId, Long groupId, Long thisUser, boolean admin);

    List<UserCourseDto> getUserByCourseIdAndGroupId(Long courseId, Long groupId, Long thisUser, boolean admin);

    UserCourseDto getUserCourseByUserIdAndCourseId(Long courseId, Long userId);

    void saveUserCourseGroup(Long groupId, Long courseId, Long thisUser, boolean admin);
}
