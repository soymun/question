package com.example.site.service;

import com.example.site.dto.ResponseExecuteSql;
import com.example.site.dto.course.*;

import java.util.List;

public interface CourseService {

    List<CourseDto> getAll(CourseRequestDto courseRequestDto, Long userId, boolean admin);

    CourseDto getById(Long id, Long userId, boolean admin);

    CourseDto saveCourse(CourseCreateDto courseCreateDto);

    CourseDto updateCourse(CourseUpdateDto courseUpdateDto, Long userId, boolean admin);

    void deleteCourse(Long id, Long userId, boolean admin);

    List<ResponseExecuteSql> executeSqlInCourse(ExecuteSqlDto requestExecuteSql, Long userId, boolean admin);
}
