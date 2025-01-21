package com.example.site.service;

import com.example.site.dto.ResponseExecuteSql;
import com.example.site.dto.course.*;

import java.util.List;

public interface CourseService {

    List<CourseDto> getAll(CourseRequestDto courseRequestDto);

    CourseDto getById(Long id);

    CourseDto saveCourse(CourseCreateDto courseCreateDto);

    CourseDto updateCourse(CourseUpdateDto courseUpdateDto);

    void deleteCourse(Long id);

    List<ResponseExecuteSql> executeSqlInCourse(ExecuteSqlDto requestExecuteSql);
}
