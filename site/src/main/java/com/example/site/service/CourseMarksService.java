package com.example.site.service;

import com.example.site.dto.marks.MarkCreateDto;
import com.example.site.dto.marks.MarkDto;

import java.util.List;

public interface CourseMarksService {

    MarkDto saveMark(MarkCreateDto markCreateDto, Long id);

    void deleteMark(Long id);

    List<MarkDto> getAllByCourse(Long id);
}
