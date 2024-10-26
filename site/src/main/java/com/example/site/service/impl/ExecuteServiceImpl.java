package com.example.site.service.impl;

import com.example.site.dto.CodeExecuteRequest;
import com.example.site.dto.RequestCheckSql;
import com.example.site.dto.task.*;
import com.example.site.exception.ForbiddenException;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.TaskMapper;
import com.example.site.model.*;
import com.example.site.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ExecuteServiceImpl {

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

    public ResultExecute execute(ExecuteDto executeDto, Long id) {

        Optional<Task> optionalTask = taskRepository.findById(executeDto.getTaskId());

        if (optionalTask.isPresent()) {

            Task task = optionalTask.get();

            return switch (task.getTaskType()) {
                case NONE, FILE -> new ResultExecute();
                case SQL -> executeSql(executeDto.getExecuteSqlDto(), executeDto.getTaskId(), id);
                case QUESTION_BOX_ONE, QUESTION_BOX_MULTI ->
                        executeBox(executeDto.getExecuteBoxDto(), executeDto.getTaskId(), id);
                case QUESTION_TEXT -> executeText(executeDto.getExecuteTextDto(), executeDto.getTaskId(), id);
                case CODE -> executeCode(executeDto.getExecuteCodeDto(), executeDto.getTaskId(), id);
            };

        } else {
            throw new NotFoundException("Задача не найдена");
        }
    }

    private ResultExecute executeSql(ExecuteSqlDto executeSqlDto, Long taskId, Long id) {

        Boolean predicate = courseRepository.getPredicateCourse(taskId);

        if (predicate == null || predicate)  {
            log.info("Execute info sql");

            TaskInfoSql taskInfoSql = taskInfoSqlRepository.getByTaskId(taskId).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(id, taskId).orElseThrow(() -> new NotFoundException("Задача не найдена"));

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
            taskRepository.updateAllAttempt(taskId);
        } else {
            throw new ForbiddenException("Задача закрыта");
        }
        return new ResultExecute();
    }

    private ResultExecute executeText(ExecuteTextDto executeBoxDto, Long taskId, Long id) {

        Boolean predicate = courseRepository.getPredicateCourse(taskId);

        if (predicate == null || predicate)  {
            taskRepository.updateAllAttempt(taskId);
            log.info("Execute info text");

            UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(id, taskId).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            userTask.setAttempt(userTask.getAttempt() + 1);

            TaskInfoQuestionText answer = taskInfoQuestionTextRepository.findByTaskId(taskId).orElseThrow(() -> new NotFoundException("Задача не найдена"));

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
                return getResultExecute(userTask, taskHistoryResult, taskId);
            }
        } else {
            throw new ForbiddenException("Задача закрыта");
        }
    }

    private ResultExecute executeBox(ExecuteBoxDto executeBoxDto, Long taskId, Long id) {

        Boolean predicate = courseRepository.getPredicateCourse(taskId);

        if (predicate == null || predicate) {
            taskRepository.updateAllAttempt(taskId);
            log.info("Execute info box");

            UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(id, taskId).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            userTask.setAttempt(userTask.getAttempt() + 1);

            List<Long> listRightAnswerIds = taskInfoQuestionBoxRepository.getRightAnswerByTaskId(taskId);

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
                return getResultExecute(userTask, taskHistoryResult, taskId);
            }
        } else {
            throw new ForbiddenException("Задача закрыта");
        }
    }

    private ResultExecute executeCode(ExecuteCodeDto executeCodeDto,Long taskId, Long id) {

        Boolean predicate = courseRepository.getPredicateCourse(taskId);

        if (predicate == null || predicate)  {
            taskRepository.updateAllAttempt(taskId);
            log.info("Execute code in user {}", id);

            TaskInfoCode taskInfoCode = taskInfoCodeRepository.getTaskInfoCodesByTaskIdAndCodeTypeId(taskId, executeCodeDto.getDcCodeTypeDto().getId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(id, taskInfoCode.getTask().getId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            userTask.setAttempt(userTask.getAttempt() + 1);

            TaskHistoryResult taskHistoryResult = new TaskHistoryResult();
            taskHistoryResult.setTask(userTask.getUserTaskId().getTask());
            taskHistoryResult.setCode(executeCodeDto.getUserCode());
            taskHistoryResult.setMessage("");
            taskHistoryResult.setRights(null);
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

        return new ResultExecute();
    }

    private ResultExecute getResultExecute(UserTask userTask, TaskHistoryResult taskHistoryResult, Long taskId) {
        userTask.setRights(true);
        taskHistoryResult.setRights(true);
        taskHistoryResult.setMessage("Ответ верен");
        taskRepository.updateRightAttempt(taskId);

        TaskHistoryResult savedResult = taskHistoryResultRepository.save(taskHistoryResult);

        CourseMarks courseMarks = courseMarksRepository.getCourseMarksByCourseIdAndCountTask(userTask.getUserTaskId().getTask().getCourses().getId(), userTask.getUserTaskId().getUser().getId());
        if (courseMarks != null) {
            UserCourse userCourse = userCourseRepository.getUserCourseByUserIdAndCourse(userTask.getUserTaskId().getUser().getId(), userTask.getUserTaskId().getTask().getCourses().getId()).orElseThrow(() -> new NotFoundException("Курс не найден"));

            userCourse.setCourseMarks(courseMarks);

            userCourseRepository.save(userCourse);
        }

        userTaskRepository.save(userTask);

        return taskMapper.taskHistoryResultToResultExecute(savedResult);
    }
}
