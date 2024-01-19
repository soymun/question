package com.example.site.service.impl;

import com.example.site.dto.task.*;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.TaskMapper;
import com.example.site.model.*;
import com.example.site.repository.*;
import com.example.site.service.TaskService;
import dto.CodeExecuteRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskHistoryResultRepository taskHistoryResultRepository;

    private final TaskInfoQuestionBoxRepository taskInfoQuestionBoxRepository;

    private final TaskInfoCodeRepository taskInfoCodeRepository;

    private final CourseMarksRepository courseMarksRepository;

    private final UserCourseRepository userCourseRepository;

    private final UserTaskRepository userTaskRepository;

    private final RabbitTemplate rabbitTemplate;

    private final TaskMapper taskMapper;

    @Override
    public TaskDto createTask(TaskCreateDto taskCreateDto) {
        if(taskCreateDto != null){
            Task task = taskMapper.taskCreateDtoToTask(taskCreateDto);
            task.setDeleted(false);
            task.setOpen(false);
            task.setAllExecute(0L);
            task.setRightExecute(0L);
            return taskMapper.taskToTaskDto(taskRepository.save(task));
        } else {
            throw new IllegalArgumentException("Невозможно создать задачу");
        }
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Не найдена задача"));
        task.setDeleted(true);
        taskRepository.save(task);
    }

    @Override
    public TaskDto updateDto(TaskUpdateDto taskUpdateDto) {
        if(taskUpdateDto != null){
            Task task = taskRepository.findById(taskUpdateDto.getId()).orElseThrow(() -> new NotFoundException("Не найдена задача"));

            ofNullable(taskUpdateDto.getTitle()).ifPresent(task::setTitle);
            ofNullable(taskUpdateDto.getDescription()).ifPresent(task::setDescription);
            ofNullable(taskUpdateDto.getName()).ifPresent(task::setName);
            ofNullable(taskUpdateDto.getNumber()).ifPresent(task::setNumber);
            ofNullable(taskUpdateDto.getOpen()).ifPresent(task::setOpen);

            return taskMapper.taskToTaskDto(taskRepository.save(task));
        } else {
            throw new IllegalArgumentException("Невозможно создать задачу");
        }
    }

    @Override
    public List<TaskDto> getAllByCourseId(Long id) {
        return taskRepository.findAllByCourseId(id).stream().map(taskMapper::taskToTaskDto).toList();
    }

    @Override
    public List<TaskDto> getWithoutCreatedTaskByCourseId(Long id) {
        return taskRepository.findAllByCourseIdWithoutCreated(id).stream().map(taskMapper::taskToTaskDto).toList();
    }

    @Override
    public List<UserTaskDto> getTaskToUserByCourse(Long userId, Long courseId) {
        return userTaskRepository.getUserTaskByUserIdAndCourseId(userId, courseId).stream().map(taskMapper::userTaskToUserTaskDto).toList();
    }

    @Override
    public TaskInfoCodeDto createTaskInfoCode(TaskInfoCodeCreateDto taskInfoCodeCreateDto) {
        if(taskInfoCodeCreateDto != null){
            TaskInfoCode taskInfoCode = taskMapper.taskInfoCodeCreateToTaskInfoCode(taskInfoCodeCreateDto);
            return taskMapper.taskInfoCodeToTaskInfoCodeDto(taskInfoCodeRepository.save(taskInfoCode));
        }else {
            throw new IllegalArgumentException("Невозможно создать задачу");
        }
    }

    @Override
    public TaskInfoCodeDto updateTaskInfoCode(TaskInfoCodeUpdateDto taskInfoCodeUpdateDto) {
        if(taskInfoCodeUpdateDto != null){
            TaskInfoCode taskInfoCode = taskInfoCodeRepository.findById(taskInfoCodeUpdateDto.getId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            ofNullable(taskInfoCodeUpdateDto.getUserClass()).ifPresent(taskInfoCode::setUserClass);
            ofNullable(taskInfoCodeUpdateDto.getCheckCode()).ifPresent(taskInfoCode::setCheckCode);
            ofNullable(taskInfoCodeUpdateDto.getInitCode()).ifPresent(taskInfoCode::setInitCode);
            ofNullable(taskInfoCodeUpdateDto.getCheckClass()).ifPresent(taskInfoCode::setCheckClass);

            return taskMapper.taskInfoCodeToTaskInfoCodeDto(taskInfoCodeRepository.save(taskInfoCode));
        }else {
            throw new IllegalArgumentException("Невозможно создать задачу");
        }
    }

    @Override
    public List<TaskInfoCodeDto> getInfoCodeByTaskId(Long id) {
        return taskInfoCodeRepository.getTaskInfoCodesByTaskId(id).stream().map(taskMapper::taskInfoCodeToTaskInfoCodeDto).toList();
    }

    @Override
    public void executeCode(ExecuteCodeDto executeCodeDto) {

        TaskInfoCode taskInfoCode = taskInfoCodeRepository.findById(executeCodeDto.getId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

        UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(executeCodeDto.getUserId(), taskInfoCode.getTask().getId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

        TaskHistoryResult taskHistoryResult = new TaskHistoryResult();
        taskHistoryResult.setTask(userTask.getUserTaskId().getTask());
        taskHistoryResult.setCode(executeCodeDto.getUserCode());
        taskHistoryResult.setMessage("");
        taskHistoryResult.setUser(userTask.getUserTaskId().getUser());

        TaskHistoryResult savedResult = taskHistoryResultRepository.save(taskHistoryResult);

        CodeExecuteRequest codeExecuteRequest = CodeExecuteRequest
                .builder()
                .attempt(userTask.getAttempt())
                .taskId(savedResult.getId())
                .checkCode(taskInfoCode.getCheckCode())
                .checkClass(taskInfoCode.getCheckClass())
                .userCode(executeCodeDto.getUserCode())
                .userClass(taskInfoCode.getUserClass())
                .userId(executeCodeDto.getUserId())
                .build();

        rabbitTemplate.convertAndSend(taskInfoCode.getCodeType().name(), codeExecuteRequest);

        userTask.setAttempt(userTask.getAttempt() + 1);
        userTaskRepository.save(userTask);
    }

    @Override
    public void deleteTaskInfoCode(Long id) {
        taskInfoCodeRepository.deleteById(id);
    }

    @Override
    public TaskInfoQuestionBoxAdminDto createTaskInfoBox(TaskInfoQuestionBoxCreateDto taskInfoQuestionBoxCreateDto) {
        if(taskInfoQuestionBoxCreateDto != null){
            return taskMapper.taskInfoBoxToAdminDto(taskInfoQuestionBoxRepository.save(taskMapper.taskInfoBoxCreateDtoToTaskInfoBox(taskInfoQuestionBoxCreateDto)));
        }else {
            throw new IllegalArgumentException("Невозможно создать задачу");
        }
    }

    @Override
    public TaskInfoQuestionBoxAdminDto updateTaskInfoBox(TaskInfoQuestionBoxUpdateDto taskInfoQuestionBoxUpdateDto) {

        if(taskInfoQuestionBoxUpdateDto != null){

            TaskInfoQuestionBox taskInfoQuestionBox = taskInfoQuestionBoxRepository.findById(taskInfoQuestionBoxUpdateDto.getId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            ofNullable(taskInfoQuestionBoxUpdateDto.getAnswer()).ifPresent(taskInfoQuestionBox::setAnswer);
            ofNullable(taskInfoQuestionBoxUpdateDto.getRights()).ifPresent(taskInfoQuestionBox::setRights);

            return taskMapper.taskInfoBoxToAdminDto(taskInfoQuestionBoxRepository.save(taskInfoQuestionBox));

        }else {
            throw new IllegalArgumentException("Невозможно создать задачу");
        }
    }

    @Override
    public List<TaskInfoQuestionBoxAdminDto> getByTaskIdAdmin(Long taskId) {
        return taskInfoQuestionBoxRepository.getByTaskId(taskId).stream().map(taskMapper::taskInfoBoxToAdminDto).toList();
    }

    @Override
    public List<TaskInfoQuestionBoxDto> getByTaskId(Long taskId) {
        return taskInfoQuestionBoxRepository.getByTaskId(taskId).stream().map(taskMapper::taskInfoBoxToDto).toList();
    }

    @Override
    public ResultExecute executeBox(ExecuteBoxDto executeBoxDto) {

        UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(executeBoxDto.getUserId(), executeBoxDto.getTaskId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

        userTask.setAttempt(userTask.getAttempt() +1);

        List<Long> listRightAnswerIds = taskInfoQuestionBoxRepository.getRightAnswerByTaskId(executeBoxDto.getTaskId());

        TaskHistoryResult taskHistoryResult = new TaskHistoryResult();
        taskHistoryResult.setTask(userTask.getUserTaskId().getTask());
        taskHistoryResult.setCode("None");
        taskHistoryResult.setUser(userTask.getUserTaskId().getUser());

        if(listRightAnswerIds.size() != executeBoxDto.getResultIds().size() || !new HashSet<>(listRightAnswerIds).containsAll(executeBoxDto.getResultIds())){

            taskHistoryResult.setRights(false);
            taskHistoryResult.setMessage("Ответ не верен");

            TaskHistoryResult savedResult = taskHistoryResultRepository.save(taskHistoryResult);

            return taskMapper.taskHistoryResultToResultExecute(savedResult);
        } else {
            userTask.setRights(true);
            taskHistoryResult.setRights(true);
            taskHistoryResult.setMessage("Ответ верен");

            TaskHistoryResult savedResult = taskHistoryResultRepository.save(taskHistoryResult);

            CourseMarks courseMarks = courseMarksRepository.getCourseMarksByCourseIdAndCountTask(userTask.getUserTaskId().getTask().getCourses().getId(), userTask.getUserTaskId().getUser().getId());
            if(courseMarks != null){
                UserCourse userCourse = userCourseRepository.getUserCourseByUserIdAndCourse(userTask.getUserTaskId().getTask().getCourses().getId(), userTask.getUserTaskId().getUser().getId()).orElseThrow(() -> new NotFoundException("Курс не найден"));

                userCourse.setCourseMarks(courseMarks);

                userCourseRepository.save(userCourse);
            }

            userTaskRepository.save(userTask);

            return taskMapper.taskHistoryResultToResultExecute(savedResult);
        }
    }

    @Override
    public void deleteTaskInfoBox(Long id) {
        taskInfoQuestionBoxRepository.deleteById(id);
    }

    @Override
    public TaskInfoQuestionTextDto createTaskInfoText(TaskInfoQuestionTextCreateDto taskInfoQuestionTextCreateDto) {
        return null;
    }

    @Override
    public TaskInfoQuestionTextDto updateTaskInfoText(TaskInfoQuestionTextUpdateDto taskInfoQuestionTextCreateDto) {
        return null;
    }

    @Override
    public ResultExecute executeText(ExecuteTextDto executeBoxDto) {
        return null;
    }

    @Override
    public void deleteTaskInfoText(Long id) {

    }

    @Override
    public TaskInfoSqlDto createTaskInfoSql(TaskInfoSqlCreateDto taskInfoSqlCreateDto) {
        return null;
    }

    @Override
    public TaskInfoSqlDto updateTaskInfoSql(TaskInfoSqlUpdateDto taskInfoSqlCreateDto) {
        return null;
    }

    @Override
    public void deleteTaskInfoSql(Long id) {

    }

    @Override
    public void executeSql(ExecuteSqlDto executeSqlDto) {

    }
}
