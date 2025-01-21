package com.example.site.service.impl;

import com.example.site.dto.usercourse.UserCourseDto;
import com.example.site.exception.ForbiddenException;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.UserCourseMapper;
import com.example.site.model.*;
import com.example.site.repository.*;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.UserCourseService;
import com.example.site.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public void saveUserCourse(Long userId, Long courseId) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        log.info("Save course");

        Courses findCourse = courseRepository.findByIdAndUserAndAdminAndUser(courseId, userDetail.getId(), userDetail.isAdmin()).orElseThrow(() -> new NotFoundException("Курс не найден"));

        userTaskRepository.saveAll(taskRepository.findAllByCourseId(findCourse.getId()).stream().map(task -> new UserTask(new UserTaskId(userId, task.getId()), false, false, 1L)).toList());

        userCourseRepository.save(new UserCourse(new UserCourseId(userId, findCourse.getId()), LocalDateTime.now(), false, false,null));
    }

    @Override
    public void saveUserCourseGroup(Long groupId, Long courseId) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        log.info("Save course");

        Courses findCourse = courseRepository.findByIdAndUserAndAdminAndUser(courseId, userDetail.getId(), userDetail.isAdmin()).orElseThrow(() -> new NotFoundException("Курс не найден"));

        userRepository.findUserByGroupId(groupId).forEach(user -> {

            userTaskRepository.saveAll(taskRepository.findAllByCourseId(findCourse.getId()).stream().map(task -> new UserTask(new UserTaskId(user.getId(), task.getId()), false, false, 1L)).toList());

            userCourseRepository.save(new UserCourse(new UserCourseId(user.getId(), findCourse.getId()), LocalDateTime.now(), false, false,null));

        });

    }

    @Override
    public void deleteCourse(Long userId, Long courseId) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        log.info("Delete course {}", courseId);

        UserCourse userCourse = userCourseRepository.getUserCourseByUserIdAndCourseAndTeacherAndAdmin(userId, courseId, userDetail.getId(), userDetail.isAdmin()).orElseThrow(() -> new NotFoundException("Курс не найден"));

        userCourse.setDeleted(true);

        userTaskRepository.saveAll(userTaskRepository.getUserTaskByUserIdAndCourseId(userId, courseId).stream().peek(ut -> ut.setClosed(true)).toList());

        userCourseRepository.save(userCourse);

    }

    @Override
    public void deleteUserCoursesByGroupId(Long courseId, Long groupId) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        log.info("Delete course {} group {}", courseId, groupId);

        List<UserCourse> userCourse = userCourseRepository.getAllByCourseIdAndGroupId(groupId, courseId, userDetail.getId(), userDetail.isAdmin()).stream().peek(uc -> uc.setDeleted(true)).toList();

        userTaskRepository.saveAll(userTaskRepository.getUserTaskByGroupIdAndCourseId(groupId, courseId).stream().peek(ut -> ut.setClosed(true)).toList());

        userCourseRepository.saveAll(userCourse);
    }

    @Override
    public List<UserCourseDto> getUserByCourseIdAndGroupId(Long courseId, Long groupId) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        return userCourseRepository
                .getAllByCourseIdAndGroupId(groupId, courseId, userDetail.getId(), userDetail.isAdmin())
                .stream()
                .map(userCourseMapper::userCourseToUserCourseDto).toList();
    }

    @Override
    public UserCourseDto getUserCourseByUserIdAndCourseId(Long courseId) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        return userCourseMapper.userCourseToUserCourseDto(userCourseRepository.findById(new UserCourseId(userDetail.getId(), courseId)).orElseThrow(() -> new  NotFoundException("Курс не найден")));
    }

    @Override
    public List<UserCourseDto> getCoursesToUser() {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        return userCourseRepository.getByUserId(userDetail.getId()).stream().map(userCourseMapper::userCourseToUserCourseDto).toList();
    }

    @Override
    public UserCourseDto addUserCourseByCourse(Long courseId){

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        return addUserCourseByUserIdAndCourseId(courseId, userDetail.getId());
    }

    @Override
    public UserCourseDto addUserCourseByUserIdAndCourseId(Long courseId, Long userId) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        Optional<Courses> optionalCourses = courseRepository.findByIdAndUserAndAdminAndUser(courseId, userId, userDetail.isAdmin());

        if (optionalCourses.isPresent()) {
            Courses courses = optionalCourses.get();

            userTaskRepository.saveAll(taskRepository.findAllByCourseId(courses.getId()).stream().map(task -> new UserTask(new UserTaskId(userId, task.getId()), false, false, 0L)).toList());

            return userCourseMapper.userCourseToUserCourseDto(userCourseRepository.save(new UserCourse(new UserCourseId(userId, courses.getId()), LocalDateTime.now(), false, false,null)));
        }

        throw new ForbiddenException("Курс не найден");
    }
}
