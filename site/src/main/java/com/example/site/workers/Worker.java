package com.example.site.workers;

import com.example.site.dto.CodeExecuteResponse;
import com.example.site.dto.ResponseCheck;
import com.example.site.dto.ResponseCheckSql;
import com.example.site.dto.Status;
import com.example.site.exception.NotFoundException;
import com.example.site.model.CourseMarks;
import com.example.site.model.TaskHistoryResult;
import com.example.site.model.UserCourse;
import com.example.site.model.UserTask;
import com.example.site.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class Worker {

    private final TaskHistoryResultRepository taskHistoryResultRepository;

    private final CourseMarksRepository courseMarksRepository;

    private final UserCourseRepository userCourseRepository;

    private final UserTaskRepository userTaskRepository;

    private final TaskRepository taskRepository;


    @RabbitListener(queues = "result", group = "result")
    @Transactional
    public void listener(ResponseCheckSql responseCheckSql) {

        log.info("Result check sql taskUserId {}", responseCheckSql.getTaskUserId());

        TaskHistoryResult taskHistoryResult = taskHistoryResultRepository.findById(responseCheckSql.getTaskUserId()).orElseThrow();

        checkAndUpdateTaskResult(responseCheckSql, taskHistoryResult);

        taskHistoryResultRepository.save(taskHistoryResult);
    }

    @RabbitListener(queues = "completed-code", group = "completed-code")
    @Transactional
    public void listenerCode(CodeExecuteResponse responseCheckCode) {

        log.info("Result check code taskUserId {}", responseCheckCode.getUserId());

        TaskHistoryResult taskHistoryResult = taskHistoryResultRepository.findById(responseCheckCode.getTaskId()).orElseThrow();

        checkAndUpdateTaskResult(responseCheckCode, taskHistoryResult);

        taskHistoryResultRepository.save(taskHistoryResult);
    }

    private void checkAndUpdateTaskResult(ResponseCheck responseCheck, TaskHistoryResult taskHistoryResult) {
        if (Status.OK.equals(responseCheck.getStatus())) {
            taskRepository.updateRightAttempt(taskHistoryResult.getTask().getId());
            taskHistoryResult.setRights(true);
            taskHistoryResult.setMessage(responseCheck.getMessage());

            UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(taskHistoryResult.getUser().getId(), taskHistoryResult.getTask().getId()).orElseThrow();

            userTask.setRights(true);

            userTaskRepository.save(userTask);

            updateResult(userTask);
        } else {
            taskHistoryResult.setRights(false);
            taskHistoryResult.setMessage(responseCheck.getMessage());
        }
    }


    private void updateResult(UserTask userTask) {
        CourseMarks courseMarks = courseMarksRepository.getCourseMarksByCourseIdAndCountTask(userTask.getUserTaskId().getTask().getCourses().getId(), userTask.getUserTaskId().getUser().getId());
        if (courseMarks != null) {
            UserCourse userCourse = userCourseRepository.getUserCourseByUserIdAndCourse(userTask.getUserTaskId().getUser().getId(), userTask.getUserTaskId().getTask().getCourses().getId()).orElseThrow(() -> new NotFoundException("Курс не найден"));

            userCourse.setCourseMarks(courseMarks);

            userCourseRepository.save(userCourse);
        }
    }
}
