package com.example.site.mappers;


import com.example.site.dto.task.*;
import com.example.site.model.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task taskCreateDtoToTask(TaskCreateDto taskCreateDto);

    TaskDto taskToTaskDto(Task task);

    TaskInfoCode taskInfoCodeCreateToTaskInfoCode(TaskInfoCodeCreateDto taskInfoCodeCreateDto);

    TaskInfoCodeDtoAdmin taskInfoCodeToTaskInfoCodeDtoAdmin(TaskInfoCode taskInfoCode);

    TaskInfoCodeDto taskInfoCodeToTaskInfoCodeDto(TaskInfoCode taskInfoCode);

    UserTaskDto userTaskToUserTaskDto(UserTask userTask);

    TaskInfoQuestionBox taskInfoBoxCreateDtoToTaskInfoBox(TaskInfoQuestionBoxCreateDto taskInfoQuestionBoxCreateDto);

    TaskInfoQuestionBoxAdminDto taskInfoBoxToAdminDto(TaskInfoQuestionBox taskInfoQuestionBox);

    TaskInfoQuestionBoxDto taskInfoBoxToDto(TaskInfoQuestionBox taskInfoQuestionBox);

    ResultExecute taskHistoryResultToResultExecute(TaskHistoryResult taskHistoryResult);

    TaskInfoQuestionText taskInfoTextCreateDtoToTaskInfoText(TaskInfoQuestionTextCreateDto taskInfoQuestionTextCreateDto);

    TaskInfoQuestionTextDto taskInfoTextToTaskInfoTextDto(TaskInfoQuestionText taskInfoQuestionText);

    TaskInfoSql taskInfoCreateSqlToTaskInfoSql(TaskInfoSqlCreateDto taskInfoSqlCreateDto);

    TaskInfoSqlAdminDto taskSqlToDtoAdmin(TaskInfoSql taskInfoSql);


    default Courses map(Long id){
        return new Courses(id);
    }

    default Long map1(Courses courses){
        if(courses != null){
            return courses.getId();
        }
        return null;
    }

    default Task map2(Long id){
        return new Task(id);
    }

    default Task map3(UserTaskId userTaskId){
        return userTaskId.getTask();
    }

    default Long map4(Task task){
        if(task == null) return null;
        return task.getId();
    }

}
