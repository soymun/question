package com.example.site.service.impl;

import com.example.site.dto.marks.MarkCreateDto;
import com.example.site.dto.marks.MarkDto;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.CourseMarkMapper;
import com.example.site.model.CourseMarks;
import com.example.site.repository.CourseMarksRepository;
import com.example.site.repository.UserCourseRepository;
import com.example.site.service.CourseMarksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CourseMarkService implements CourseMarksService {

    private final CourseMarksRepository courseMarksRepository;
    private final UserCourseRepository userCourseRepository;

    private final CourseMarkMapper courseMarkMapper;

    @Override
    public MarkDto saveMark(MarkCreateDto markCreateDto) {
        if(markCreateDto != null){

            CourseMarks courseMarks = courseMarksRepository.save(courseMarkMapper.markCreateDtoToCourseMarks(markCreateDto));

            userCourseRepository.saveAll(userCourseRepository.getUserCourseByCountTask(courseMarks.getCountTask(), courseMarks.getCourses().getId()).stream().peek(u -> u.setCourseMarks(courseMarks)).toList());

            return courseMarkMapper.courseMarkToMarkDto(courseMarks);

        }
        throw new IllegalArgumentException("Не валидные данные");
    }

    @Override
    public void deleteMark(Long id) {
        CourseMarks courseMarks = courseMarksRepository.findById(id).orElseThrow(() -> new NotFoundException("Оценка не найдена"));

        CourseMarks lessMark = courseMarksRepository.getCourseMarksLessCountByCourseId(courseMarks.getCourses().getId(), courseMarks.getCountTask());

        userCourseRepository.saveAll(userCourseRepository.getUserCourseByCourseId(courseMarks.getCourses().getId(), courseMarks.getCountTask()).stream().peek(userCourse -> userCourse.setCourseMarks(lessMark)).toList());

        courseMarksRepository.delete(courseMarks);
    }

    @Override
    public List<MarkDto> getAllByCourse(Long id) {
        return courseMarksRepository.getCourseMarksByCourseId(id).stream().map(courseMarkMapper::courseMarkToMarkDto).toList();
    }
}
