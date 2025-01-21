package com.example.site.service;

import com.example.site.dto.usercourse.UserCourseDto;

import java.util.List;

public interface UserCourseService {

    void saveUserCourse(Long userId, Long courseId);

    void deleteCourse(Long userId, Long courseId);

    void deleteUserCoursesByGroupId(Long courseId, Long groupId);

    List<UserCourseDto> getUserByCourseIdAndGroupId(Long courseId, Long groupId);

    UserCourseDto getUserCourseByUserIdAndCourseId(Long courseId);

    void saveUserCourseGroup(Long groupId, Long courseId);

    UserCourseDto addUserCourseByCourse(Long courseId);

    UserCourseDto addUserCourseByUserIdAndCourseId(Long courseId, Long userId);

    List<UserCourseDto> getCoursesToUser();
}
