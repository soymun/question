package com.example.site.service.impl;

import com.example.site.dto.course.CourseCreateDto;
import com.example.site.dto.course.CourseDto;
import com.example.site.dto.course.CourseUpdateDto;
import com.example.site.dto.course.ExecuteSqlDto;
import com.example.site.exception.ForbiddenException;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.CourseMapper;
import com.example.site.model.Courses;
import com.example.site.repository.CourseRepository;
import com.example.site.service.CourseService;
import dto.RequestCreateSchema;
import dto.RequestExecuteSql;
import dto.ResponseCreateSchema;
import dto.ResponseExecuteSql;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CourseServiceImpl implements CourseService {

    private final RabbitTemplate rabbitTemplate;

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    @Override
    public List<CourseDto> getAll(int pageNumber, int pageSize, Long userId, boolean admin) {
        return courseRepository.getAll(userId, admin, PageRequest.of(pageNumber, pageSize)).get().map(courseMapper::courseToCourseDto).toList();
    }

    @Override
    public List<CourseDto> getByQuery(String name, int pageNumber, int pageSize, Long userId, boolean admin) {
        return courseRepository.getCoursesByQuery(name, userId, admin, PageRequest.of(pageNumber, pageSize)).get().map(courseMapper::courseToCourseDto).toList();
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
            Courses courses = courseMapper.courseCreateDtoToCourse(courseCreateDto);
            courses.setTimeCreated(LocalDate.now());
            Courses savedCourse = courseRepository.save(courses);
            savedCourse.setSchema(((ResponseCreateSchema) Objects.requireNonNull(rabbitTemplate.convertSendAndReceive("schema", RequestCreateSchema.builder().courseId(savedCourse.getId()).build()))).getSchema());
            return courseMapper.courseToCourseDto(courseRepository.save(savedCourse));
        }
        throw new IllegalArgumentException("Курс не валиден");
    }

    @Override
    public CourseDto updateCourse(CourseUpdateDto courseUpdateDto, Long userId, boolean admin) {
        if (courseUpdateDto != null) {
            Courses courses = courseRepository.findById(courseUpdateDto.getId()).orElseThrow(() -> new NotFoundException("Курс не найден"));
            if (admin || courses.getUserCreated().getId().equals(userId)) {
                ofNullable(courseUpdateDto.getCourseName()).ifPresent(courses::setCourseName);
                ofNullable(courseUpdateDto.getAbout()).ifPresent(courses::setAbout);
                ofNullable(courseUpdateDto.getCourseType()).ifPresent(courses::setCourseType);
                ofNullable(courseUpdateDto.getPathImage()).ifPresent(courses::setPathImage);
                return courseMapper.courseToCourseDto(courseRepository.save(courses));
            }
            throw new ForbiddenException("Изменение не доступно");
        }
        throw new IllegalArgumentException("Курс не валиден");
    }

    @Override
    public void deleteCourse(Long id, Long userId, boolean admin) {
        Courses courses = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Курс не найден"));
        if (admin || courses.getUserCreated().getId().equals(userId)) {
            courses.setDeleted(true);
            courseRepository.save(courses);
        }
        throw new ForbiddenException("Удаление не доступно");
    }

    @Override
    public List<CourseDto> getAllByTeacher(Long id, int pageNumber, int pageSize) {
        return courseRepository.getAllByTeacherId(id, PageRequest.of(pageNumber, pageSize)).get().map(courseMapper::courseToCourseDto).toList();
    }

    @Override
    public List<ResponseExecuteSql> executeSqlInCourse(ExecuteSqlDto requestExecuteSql, Long userId, boolean admin) {
        Courses courses = courseRepository.findById(requestExecuteSql.getCourseId()).orElseThrow(() -> new NotFoundException("Курс не найден"));
        boolean adminSql = admin || courses.getUserCreated().getId().equals(userId);
        return (List<ResponseExecuteSql>) rabbitTemplate.convertSendAndReceive(RequestExecuteSql
                .builder()
                .admin(adminSql)
                .userId(userId)
                .schema(courses.getSchema())
                .userSql(requestExecuteSql.getUserSql())
                .build());
    }
}
