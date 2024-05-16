package com.example.site.service.impl;

import com.example.site.dto.usercourse.UserCourseDto;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.UserCourseMapper;
import com.example.site.model.*;
import com.example.site.repository.*;
import com.example.site.service.UserCourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    private final UserRepository userRepository;


    @Override
    public void saveUserCourse(Long userId, Long courseId, Long thisUser, boolean admin) {

        log.info("Save course");

        Courses findCourse = courseRepository.findByIdAndUserAndAdminAndUser(courseId, thisUser, admin).orElseThrow(() -> new NotFoundException("Курс не найден"));

        userTaskRepository.saveAll(taskRepository.findAllByCourseId(findCourse.getId()).stream().map(task -> new UserTask(new UserTaskId(userId, task.getId()), false, false, 1L)).toList());

        userCourseRepository.save(new UserCourse(new UserCourseId(userId, findCourse.getId()), LocalDateTime.now(), false, false,null));
    }

    @Override
    public void saveUserCourseGroup(Long groupId, Long courseId, Long thisUser, boolean admin) {

        log.info("Save course");

        Courses findCourse = courseRepository.findByIdAndUserAndAdminAndUser(courseId, thisUser, admin).orElseThrow(() -> new NotFoundException("Курс не найден"));

        userRepository.findUserByGroupId(groupId).forEach(user -> {

            userTaskRepository.saveAll(taskRepository.findAllByCourseId(findCourse.getId()).stream().map(task -> new UserTask(new UserTaskId(user.getId(), task.getId()), false, false, 1L)).toList());

            userCourseRepository.save(new UserCourse(new UserCourseId(user.getId(), findCourse.getId()), LocalDateTime.now(), false, false,null));

        });

    }

    @Override
    public void deleteCourse(Long userId, Long courseId, Long thisUser, boolean admin) {

        log.info("Delete course {}", courseId);

        UserCourse userCourse = userCourseRepository.getUserCourseByUserIdAndCourseAndTeacherAndAdmin(userId, courseId, thisUser, admin).orElseThrow(() -> new NotFoundException("Курс не найден"));

        userCourse.setDeleted(true);

        userTaskRepository.saveAll(userTaskRepository.getUserTaskByUserIdAndCourseId(userId, courseId).stream().peek(ut -> ut.setClosed(true)).toList());

        userCourseRepository.save(userCourse);

    }

    @Override
    public void deleteUserCoursesByGroupId(Long courseId, Long groupId, Long thisUser, boolean admin) {

        log.info("Delete course {} group {}", courseId, groupId);

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
