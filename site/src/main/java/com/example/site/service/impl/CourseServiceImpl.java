package com.example.site.service.impl;

import com.example.site.dto.RequestExecuteSql;
import com.example.site.dto.ResponseExecuteSql;
import com.example.site.dto.course.*;
import com.example.site.exception.ForbiddenException;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.CourseMapper;
import com.example.site.model.Courses;
import com.example.site.model.User;
import com.example.site.model.UserCourse;
import com.example.site.model.UserCourseId;
import com.example.site.model.util.CourseType;
import com.example.site.repository.CourseRepository;
import com.example.site.repository.UserCourseRepository;
import com.example.site.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CourseServiceImpl implements CourseService {

    private final RabbitTemplate rabbitTemplate;

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    private final UserCourseRepository userCourseRepository;

    @Override
    public List<CourseDto> getAll(CourseRequestDto courseRequestDto, Long userId, boolean admin) {
        return courseRepository.getAll(userId, courseRequestDto.getQuery(), userId, admin, courseRequestDto.getTeacher()).stream()
                .map(courseMapper::courseToCourseDto)
                .toList();
    }

    @Override
    public CourseDto getById(Long id, Long userId, boolean admin) {
        Courses courses = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Курс не найден"));
        if (admin || courses.getUserCreated().getId().equals(userId)) {
            return courseMapper.courseToCourseDto(courses);
        }
        throw new ForbiddenException("Курс не доступен");
    }

    @Override
    public CourseDto saveCourse(CourseCreateDto courseCreateDto) {
        if (courseCreateDto != null) {

            log.info("Save course");

            Courses courses = courseMapper.courseCreateDtoToCourse(courseCreateDto);
            courses.setDeleted(false);
            if (CourseType.USUALLY.equals(courses.getCourseType())) {
                courses.setTimeExecute(null);
            }

            CourseDto courseDto = courseMapper.courseToCourseDto(courseRepository.save(courses));
            UserCourse userCourse = new UserCourse();
            UserCourseId userCourseId = new UserCourseId();
            userCourseId.setUser(new User(courseCreateDto.getUserCreated()));
            userCourseId.setCourses(new Courses(courseDto.getId()));
            userCourse.setUserCourseId(userCourseId);
            userCourse.setDeleted(false);
            userCourse.setClosed(false);
            userCourse.setStartDate(LocalDateTime.now());
            userCourseRepository.save(userCourse);

            return courseMapper.courseToCourseDto(courseRepository.save(courses));
        }
        throw new IllegalArgumentException("Курс не валиден");
    }

    @Override
    public CourseDto updateCourse(CourseUpdateDto courseUpdateDto, Long userId, boolean admin) {
        if (courseUpdateDto != null) {
            log.info("Update course {}", courseUpdateDto.getId());
            Courses courses = courseRepository.findById(courseUpdateDto.getId()).orElseThrow(() -> new NotFoundException("Курс не найден"));
            if (admin || courses.getUserCreated().getId().equals(userId)) {
                ofNullable(courseUpdateDto.getCourseName()).ifPresent(courses::setCourseName);
                ofNullable(courseUpdateDto.getAbout()).ifPresent(courses::setAbout);
                ofNullable(courseUpdateDto.getCourseType()).ifPresent(courses::setCourseType);
                ofNullable(courseUpdateDto.getPathImage()).ifPresent(courses::setPathImage);
                ofNullable(courseUpdateDto.getOpen()).ifPresent(courses::setOpen);
                ofNullable(courseUpdateDto.getTimeExecute()).ifPresent(courses::setTimeExecute);
                return courseMapper.courseToCourseDto(courseRepository.save(courses));
            }
            throw new ForbiddenException("Изменение не доступно");
        }
        throw new IllegalArgumentException("Курс не валиден");
    }

    @Override
    public void deleteCourse(Long id, Long userId, boolean admin) {

        log.info("Delete course {}", id);

        Courses courses = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Курс не найден"));
        if (admin || courses.getUserCreated().getId().equals(userId)) {
            courses.setDeleted(true);
            courseRepository.save(courses);
            userCourseRepository.saveAll(userCourseRepository.getUserCourseByCourseId(courses.getId()).stream().peek(item -> item.setClosed(true)).collect(Collectors.toList()));
            return;
        }
        throw new ForbiddenException("Удаление не доступно");
    }

    @Override
    public List<ResponseExecuteSql> executeSqlInCourse(ExecuteSqlDto requestExecuteSql, Long userId, boolean admin) {

        log.info("Execute sql");

        Courses courses = courseRepository.findById(requestExecuteSql.getCourseId()).orElseThrow(() -> new NotFoundException("Курс не найден"));
        if (courses.getSchema() != null) {
            boolean adminSql = admin || courses.getUserCreated().getId().equals(userId);
            return rabbitTemplate.convertSendAndReceiveAsType(courses.getSqlType().getValue().toLowerCase() + "-execute", RequestExecuteSql
                    .builder()
                    .admin(adminSql)
                    .userId(userId)
                    .schema(courses.getSchema())
                    .userSql(requestExecuteSql.getUserSql())
                    .build(), new ParameterizedTypeReference<List<ResponseExecuteSql>>() {
                @Override
                public Type getType() {
                    return List.class;
                }
            });
        } else {
            throw new ForbiddenException("Создайте задачу по sql");
        }
    }
}
