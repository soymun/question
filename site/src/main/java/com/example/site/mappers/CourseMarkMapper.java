package com.example.site.mappers;

import com.example.site.dto.marks.MarkCreateDto;
import com.example.site.dto.marks.MarkDto;
import com.example.site.model.CourseMarks;
import com.example.site.model.Courses;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMarkMapper {

    CourseMarks markCreateDtoToCourseMarks(MarkCreateDto markCreateDto);

    MarkDto courseMarkToMarkDto(CourseMarks courseMarks);

    default Courses map(Long id){
        return new Courses(id);
    }

    default Long map1(Courses courses){
        if(courses != null){
            return courses.getId();
        }
        return null;
    }
}
