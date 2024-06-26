package com.example.site.service;

import com.example.site.dto.task.*;

import java.util.List;

public interface TaskService {

    TaskDto createTask(TaskCreateDto taskCreateDto);

    void deleteTask(Long id);

    TaskDto updateDto(TaskUpdateDto taskUpdateDto);

    List<TaskDto> getAllByCourseId(Long id);

    List<UserTaskDto> getTaskToUserByCourse(Long userId, Long courseId);

    TaskInfoCodeDtoAdmin createTaskInfoCode(TaskInfoCodeCreateDto taskInfoCodeCreateDto);

    TaskInfoCodeDtoAdmin updateTaskInfoCode(TaskInfoCodeUpdateDto taskInfoCodeUpdateDto);

    List<TaskInfoCodeDtoAdmin> getInfoCodeByTaskIdAdmin(Long id);

    List<TaskInfoCodeDto> getInfoCodeByTaskId(Long id);

    void deleteTaskInfoCode(Long id);

    TaskInfoQuestionBoxAdminDto createTaskInfoBox(TaskInfoQuestionBoxCreateDto taskInfoQuestionBoxCreateDto);

    TaskInfoQuestionBoxAdminDto updateTaskInfoBox(TaskInfoQuestionBoxUpdateDto taskInfoQuestionBoxUpdateDto);

    List<TaskInfoQuestionBoxAdminDto> getByTaskIdAdmin(Long courseId);

    List<TaskInfoQuestionBoxDto> getByTaskId(Long courseId);

    void deleteTaskInfoBox(Long id);

    TaskInfoQuestionTextDto createTaskInfoText(TaskInfoQuestionTextCreateDto taskInfoQuestionTextCreateDto);

    TaskInfoQuestionTextDto updateTaskInfoText(TaskInfoQuestionTextUpdateDto taskInfoQuestionTextCreateDto);

    TaskInfoQuestionTextDto getTextByTaskId(Long taskId);

    void deleteTaskInfoText(Long id);

    TaskInfoSqlAdminDto createTaskInfoSql(TaskInfoSqlCreateDto taskInfoSqlCreateDto);

    TaskInfoSqlAdminDto updateTaskInfoSql(TaskInfoSqlUpdateDto taskInfoSqlCreateDto);

    void deleteTaskInfoSql(Long id);

    TaskInfoSqlAdminDto getSqlAdminByTaskId(Long id);
}
