package com.example.site.service.impl;

import com.example.site.dto.user_course.UserCourseDto;
import com.example.site.dto.user_course.UserCourseUpdate;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.UserCourseMapper;
import com.example.site.model.*;
import com.example.site.repository.CourseRepository;
import com.example.site.repository.TaskRepository;
import com.example.site.repository.UserCourseRepository;
import com.example.site.repository.UserTaskRepository;
import com.example.site.service.UserCourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserCourseServiceImpl implements UserCourseService {

    private final CourseRepository courseRepository;

    private final UserCourseRepository userCourseRepository;

    private final TaskRepository taskRepository;

    private final UserTaskRepository userTaskRepository;

    private final UserCourseMapper userCourseMapper;


    @Override
    public void saveUserCourse(Long userId, Long courseId, Long thisUser, boolean admin) {

        Courses findCourse = courseRepository.findByIdAndUserAndAdminAndUser(courseId, thisUser, admin).orElseThrow(() -> new NotFoundException("Курс не найден"));

        userTaskRepository.saveAll(taskRepository.findAllByCourseId(findCourse.getId()).stream().map(task -> new UserTask(new UserTaskId(userId, task.getId()))).toList());

        userCourseRepository.save(new UserCourse(new UserCourseId(userId, findCourse.getId())));
    }

    @Override
    public void updateUserCourse(UserCourseUpdate userCourseUpdate) {

        UserCourse userCourse = userCourseRepository.findById(new UserCourseId(userCourseUpdate.getUserId(), userCourseUpdate.getCourseId())).orElseThrow(() -> new NotFoundException("Курс не найден"));

        ofNullable(userCourseUpdate.getStartCourse()).ifPresent(userCourse::setStartDate);
        ofNullable(userCourseUpdate.getEndCourse()).ifPresent(userCourse::setEndDate);

        userCourseRepository.save(userCourse);
    }

    @Override
    public void deleteCourse(Long userId, Long courseId, Long thisUser, boolean admin) {

        UserCourse userCourse = userCourseRepository.getUserCourseByUserIdAndCourseAndTeacherAndAdmin(userId, courseId, thisUser, admin).orElseThrow(() -> new NotFoundException("Курс не найден"));

        userCourse.setDeleted(true);

        userTaskRepository.saveAll(userTaskRepository.getUserTaskByUserIdAndCourseId(userId, courseId).stream().peek(ut -> ut.setClosed(true)).toList());

        userCourseRepository.save(userCourse);

    }

    @Override
    public void deleteUserCoursesByGroupId(Long courseId, Long groupId, Long thisUser, boolean admin) {

        List<UserCourse> userCourse = userCourseRepository.getAllByCourseIdAndGroupId(groupId, courseId, thisUser, admin).stream().peek(uc -> uc.setDeleted(true)).toList();

        userTaskRepository.saveAll(userTaskRepository.getUserTaskByGroupIdAndCourseId(groupId, courseId).stream().peek(ut -> ut.setClosed(true)).toList());

        userCourseRepository.saveAll(userCourse);
    }

    @Override
    public List<UserCourseDto> getUserByCourseIdAndGroupId(Long courseId, Long groupId, Long thisUser, boolean admin) {
        return userCourseRepository.getAllByCourseIdAndGroupId(groupId, courseId, thisUser, admin).stream().map(userCourseMapper::userCourseToUserCourseDto).toList();
    }

    @Override
    public UserCourseDto getUserCourseByUserIdAndCourseId(Long courseId, Long userId) {
        return userCourseMapper.userCourseToUserCourseDto(userCourseRepository.findById(new UserCourseId(userId, courseId)).orElseThrow(() -> new  NotFoundException("Курс не найден")));
    }
}
