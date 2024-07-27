package com.example.site.service.impl;

import com.example.site.dto.*;
import com.example.site.dto.task.*;
import com.example.site.exception.ForbiddenException;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.TaskMapper;
import com.example.site.model.*;
import com.example.site.repository.*;
import com.example.site.service.ExecuteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ExecuteServiceImpl implements ExecuteService {

    private final TaskHistoryResultRepository taskHistoryResultRepository;

    private final TaskInfoQuestionBoxRepository taskInfoQuestionBoxRepository;

    private final TaskInfoCodeRepository taskInfoCodeRepository;

    private final CourseMarksRepository courseMarksRepository;

    private final UserCourseRepository userCourseRepository;

    private final UserTaskRepository userTaskRepository;

    private final RabbitTemplate rabbitTemplate;

    private final TaskMapper taskMapper;

    private final TaskInfoQuestionTextRepository taskInfoQuestionTextRepository;

    private final TaskInfoSqlRepository taskInfoSqlRepository;

    private final CourseRepository courseRepository;

    private final TaskRepository taskRepository;

    public void executeSql(ExecuteSqlDto executeSqlDto, Long id) {

        if (courseRepository.getPredicateCourse(executeSqlDto.getTaskId())) {
            log.info("Execute info sql");

            TaskInfoSql taskInfoSql = taskInfoSqlRepository.getByTaskId(executeSqlDto.getTaskId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(id, executeSqlDto.getTaskId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            TaskHistoryResult taskHistoryResult = new TaskHistoryResult();
            taskHistoryResult.setTask(userTask.getUserTaskId().getTask());
            taskHistoryResult.setCode(executeSqlDto.getUserSql());
            taskHistoryResult.setMessage("");
            taskHistoryResult.setUser(userTask.getUserTaskId().getUser());
            taskHistoryResult.setTimeResult(LocalDateTime.now());

            TaskHistoryResult savedResult = taskHistoryResultRepository.save(taskHistoryResult);

            RequestCheckSql request = RequestCheckSql
                    .builder()
                    .checkSql(taskInfoSql.getCheckSql())
                    .mainSql(taskInfoSql.getMainSql())
                    .userSql(executeSqlDto.getUserSql())
                    .taskUserId(savedResult.getId())
                    .schema(courseRepository.findById(userTask.getUserTaskId().getTask().getCourses().getId()).orElseThrow(() -> new NotFoundException("Курс не найден")).getSchema())
                    .build();

            rabbitTemplate.convertAndSend("check", request);

            userTask.setAttempt(userTask.getAttempt() + 1);
            userTaskRepository.save(userTask);
            taskRepository.updateAllAttempt(executeSqlDto.getTaskId());
        } else {
            throw new ForbiddenException("Задача закрыта");
        }
    }

    public ResultExecute executeText(ExecuteTextDto executeBoxDto, Long id) {

        if (courseRepository.getPredicateCourse(executeBoxDto.getTaskId())) {
            taskRepository.updateAllAttempt(executeBoxDto.getTaskId());
            log.info("Execute info text");

            UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(id, executeBoxDto.getTaskId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            userTask.setAttempt(userTask.getAttempt() + 1);

            TaskInfoQuestionText answer = taskInfoQuestionTextRepository.findByTaskId(executeBoxDto.getTaskId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            TaskHistoryResult taskHistoryResult = new TaskHistoryResult();
            taskHistoryResult.setTask(userTask.getUserTaskId().getTask());
            taskHistoryResult.setCode("None");
            taskHistoryResult.setUser(userTask.getUserTaskId().getUser());
            taskHistoryResult.setTimeResult(LocalDateTime.now());

            if (!answer.getAnswer().toLowerCase().trim().equals(executeBoxDto.getAnswer())) {

                taskHistoryResult.setRights(false);
                taskHistoryResult.setMessage("Ответ не верен");

                TaskHistoryResult savedResult = taskHistoryResultRepository.save(taskHistoryResult);

                return taskMapper.taskHistoryResultToResultExecute(savedResult);
            } else {
                return getResultExecute(userTask, taskHistoryResult, executeBoxDto.getTaskId());
            }
        } else {
            throw new ForbiddenException("Задача закрыта");
        }
    }

    public ResultExecute executeBox(ExecuteBoxDto executeBoxDto, Long id) {

        if (courseRepository.getPredicateCourse(executeBoxDto.getTaskId())) {
            taskRepository.updateAllAttempt(executeBoxDto.getTaskId());
            log.info("Execute info box");

            UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(id, executeBoxDto.getTaskId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            userTask.setAttempt(userTask.getAttempt() + 1);

            List<Long> listRightAnswerIds = taskInfoQuestionBoxRepository.getRightAnswerByTaskId(executeBoxDto.getTaskId());

            TaskHistoryResult taskHistoryResult = new TaskHistoryResult();
            taskHistoryResult.setTask(userTask.getUserTaskId().getTask());
            taskHistoryResult.setCode("None");
            taskHistoryResult.setUser(userTask.getUserTaskId().getUser());
            taskHistoryResult.setTimeResult(LocalDateTime.now());

            if (listRightAnswerIds.size() != executeBoxDto.getResultIds().size() || !new HashSet<>(listRightAnswerIds).containsAll(executeBoxDto.getResultIds())) {

                taskHistoryResult.setRights(false);
                taskHistoryResult.setMessage("Ответ не верен");

                TaskHistoryResult savedResult = taskHistoryResultRepository.save(taskHistoryResult);

                return taskMapper.taskHistoryResultToResultExecute(savedResult);
            } else {
                return getResultExecute(userTask, taskHistoryResult, executeBoxDto.getTaskId());
            }
        } else {
            throw new ForbiddenException("Задача закрыта");
        }
    }

    public void executeCode(ExecuteCodeDto executeCodeDto, Long id) {

        if (courseRepository.getPredicateCourse(executeCodeDto.getTaskId())) {
            taskRepository.updateAllAttempt(executeCodeDto.getTaskId());
            log.info("Execute code in user {}", id);

            TaskInfoCode taskInfoCode = taskInfoCodeRepository.findById(executeCodeDto.getTaskId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(id, taskInfoCode.getTask().getId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            userTask.setAttempt(userTask.getAttempt() + 1);

            TaskHistoryResult taskHistoryResult = new TaskHistoryResult();
            taskHistoryResult.setTask(userTask.getUserTaskId().getTask());
            taskHistoryResult.setCode(executeCodeDto.getUserCode());
            taskHistoryResult.setMessage("");
            taskHistoryResult.setRights(false);
            taskHistoryResult.setUser(userTask.getUserTaskId().getUser());
            taskHistoryResult.setTimeResult(LocalDateTime.now());

            TaskHistoryResult savedResult = taskHistoryResultRepository.save(taskHistoryResult);

            CodeExecuteRequest codeExecuteRequest = CodeExecuteRequest
                    .builder()
                    .attempt(userTask.getAttempt())
                    .taskId(savedResult.getId())
                    .checkCode(taskInfoCode.getCheckCode())
                    .checkClass(taskInfoCode.getCheckClass())
                    .userCode(executeCodeDto.getUserCode())
                    .userClass(taskInfoCode.getUserClass())
                    .userId(id)
                    .build();

            rabbitTemplate.convertAndSend(taskInfoCode.getCodeType().getName(), codeExecuteRequest);

            userTaskRepository.save(userTask);
        } else {
            throw new ForbiddenException("Задача закрыта");
        }
    }

    private ResultExecute getResultExecute(UserTask userTask, TaskHistoryResult taskHistoryResult, Long taskId) {
        userTask.setRights(true);
        taskHistoryResult.setRights(true);
        taskHistoryResult.setMessage("Ответ верен");
        taskRepository.updateRightAttempt(taskId);

        TaskHistoryResult savedResult = taskHistoryResultRepository.save(taskHistoryResult);

        CourseMarks courseMarks = courseMarksRepository.getCourseMarksByCourseIdAndCountTask(userTask.getUserTaskId().getTask().getCourses().getId(), userTask.getUserTaskId().getUser().getId());
        if (courseMarks != null) {
            UserCourse userCourse = userCourseRepository.getUserCourseByUserIdAndCourse(userTask.getUserTaskId().getTask().getCourses().getId(), userTask.getUserTaskId().getUser().getId()).orElseThrow(() -> new NotFoundException("Курс не найден"));

            userCourse.setCourseMarks(courseMarks);

            userCourseRepository.save(userCourse);
        }

        userTaskRepository.save(userTask);

        return taskMapper.taskHistoryResultToResultExecute(savedResult);
    }
}
