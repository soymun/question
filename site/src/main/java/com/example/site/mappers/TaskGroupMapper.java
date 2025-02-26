package com.example.site.mappers;

import com.example.site.dto.task_group.TaskGroupDto;
import com.example.site.model.Courses;
import com.example.site.model.TaskGroup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskGroupMapper {

    TaskGroup dtoToEntity(TaskGroupDto dto);

    TaskGroupDto entityToDto(TaskGroup dto);

    default Long entityToLong(Courses courses) {
        return courses.getId();
    }

    default Courses longToEntity(Long coursesId) {
        return new Courses(coursesId);
    }
}
