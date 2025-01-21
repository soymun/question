package com.example.site.service.impl;

import com.example.site.dto.marks.MarkCreateDto;
import com.example.site.dto.marks.MarkDto;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.CourseMarkMapper;
import com.example.site.model.CourseMarks;
import com.example.site.model.User;
import com.example.site.repository.CourseMarksRepository;
import com.example.site.repository.UserCourseRepository;
import com.example.site.repository.UserRepository;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.CourseMarksService;
import com.example.site.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CourseMarkServiceImpl implements CourseMarksService {

    private final CourseMarksRepository courseMarksRepository;

    private final UserCourseRepository userCourseRepository;

    private final CourseMarkMapper courseMarkMapper;

    private final UserRepository userRepository;

    @Override
    public MarkDto saveMark(MarkCreateDto markCreateDto) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        if(markCreateDto != null){

            log.info("Create mark at courses {}", markCreateDto.getCourses());

            CourseMarks courseMarks = courseMarkMapper.markCreateDtoToCourseMarks(markCreateDto);

            courseMarks.setUserCreated(userRepository.getReferenceById(userDetail.getId()));

            CourseMarks courseMarksSaved = courseMarksRepository.save(courseMarks);

            userCourseRepository.saveAll(userCourseRepository.getUserCourseByCountTask(courseMarksSaved.getCountTask(), courseMarksSaved.getCourses().getId()).stream().peek(u -> u.setCourseMarks(courseMarksSaved)).toList());

            return courseMarkMapper.courseMarkToMarkDto(courseMarksSaved);

        }
        throw new IllegalArgumentException("Не валидные данные");
    }

    @Override
    public void deleteMark(Long id) {

        log.info("Delete mark {}", id);

        CourseMarks courseMarks = courseMarksRepository.findById(id).orElseThrow(() -> new NotFoundException("Оценка не найдена"));

        CourseMarks lessMark = courseMarksRepository.getCourseMarksLessCountByCourseId(courseMarks.getCourses().getId());

        userCourseRepository.saveAll(userCourseRepository.getUserCourseByCourseIdAndMark(courseMarks.getCourses().getId(), courseMarks.getCountTask()).stream().peek(userCourse -> userCourse.setCourseMarks(lessMark)).toList());

        courseMarksRepository.delete(courseMarks);
    }

    @Override
    public List<MarkDto> getAllByCourse(Long id) {
        return courseMarksRepository.getCourseMarksByCourseId(id).stream().map(courseMarkMapper::courseMarkToMarkDto).toList();
    }
}
