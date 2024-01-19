package com.example.site.service;

import com.example.site.dto.task.*;

import java.util.List;

public interface TaskService {

    TaskDto createTask(TaskCreateDto taskCreateDto);

    void deleteTask(Long id);

    TaskDto updateDto(TaskUpdateDto taskUpdateDto);

    List<TaskDto> getAllByCourseId(Long id);

    List<TaskDto> getWithoutCreatedTaskByCourseId(Long id);

    List<UserTaskDto> getTaskToUserByCourse(Long userId, Long courseId);

    TaskInfoCodeDto createTaskInfoCode(TaskInfoCodeCreateDto taskInfoCodeCreateDto);

    TaskInfoCodeDto updateTaskInfoCode(TaskInfoCodeUpdateDto taskInfoCodeUpdateDto);

    List<TaskInfoCodeDto> getInfoCodeByTaskId(Long id);

    void executeCode(ExecuteCodeDto executeCodeDto);

    void deleteTaskInfoCode(Long id);

    TaskInfoQuestionBoxAdminDto createTaskInfoBox(TaskInfoQuestionBoxCreateDto taskInfoQuestionBoxCreateDto);

    TaskInfoQuestionBoxAdminDto updateTaskInfoBox(TaskInfoQuestionBoxUpdateDto taskInfoQuestionBoxUpdateDto);

    List<TaskInfoQuestionBoxAdminDto> getByTaskIdAdmin(Long courseId);

    List<TaskInfoQuestionBoxDto> getByTaskId(Long courseId);

    ResultExecute executeBox(ExecuteBoxDto executeBoxDto);

    void deleteTaskInfoBox(Long id);

    TaskInfoQuestionTextDto createTaskInfoText(TaskInfoQuestionTextCreateDto taskInfoQuestionTextCreateDto);

    TaskInfoQuestionTextDto updateTaskInfoText(TaskInfoQuestionTextUpdateDto taskInfoQuestionTextCreateDto);

    ResultExecute executeText(ExecuteTextDto executeBoxDto);

    void deleteTaskInfoText(Long id);

    TaskInfoSqlDto createTaskInfoSql(TaskInfoSqlCreateDto taskInfoSqlCreateDto);

    TaskInfoSqlDto updateTaskInfoSql(TaskInfoSqlUpdateDto taskInfoSqlCreateDto);

    void deleteTaskInfoSql(Long id);

    void executeSql(ExecuteSqlDto executeSqlDto);

}
