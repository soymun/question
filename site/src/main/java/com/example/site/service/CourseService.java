package com.example.site.service;

import com.example.site.dto.course.CourseCreateDto;
import com.example.site.dto.course.CourseDto;
import com.example.site.dto.course.CourseUpdateDto;
import com.example.site.dto.course.ExecuteSqlDto;
import dto.RequestExecuteSql;
import dto.ResponseExecuteSql;

import java.util.List;

public interface CourseService {

    List<CourseDto> getAll(int pageNumber, int pageSize, Long userId, boolean admin);

    List<CourseDto> getByQuery(String name, int pageNumber, int pageSize, Long userId, boolean admin);

    CourseDto getById(Long id, Long userId, boolean admin);

    CourseDto saveCourse(CourseCreateDto courseCreateDto);

    CourseDto updateCourse(CourseUpdateDto courseUpdateDto, Long userId, boolean admin);

    void deleteCourse(Long id, Long userId, boolean admin);

    List<CourseDto> getAllByTeacher(Long id, int pageNumber, int pageSize);

    List<ResponseExecuteSql> executeSqlInCourse(ExecuteSqlDto requestExecuteSql, Long userId, boolean admin);
}
