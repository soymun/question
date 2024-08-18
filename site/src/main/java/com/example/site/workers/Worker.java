package com.example.site.workers;

import com.example.site.dto.*;
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

        log.info("Result check sql");

        TaskHistoryResult taskHistoryResult = taskHistoryResultRepository.findById(responseCheckSql.getTaskUserId()).orElseThrow();

        if (responseCheckSql.getStatus().equals(Status.OK)) {
            taskRepository.updateRightAttempt(taskHistoryResult.getTask().getId());
            taskHistoryResult.setRights(true);
            taskHistoryResult.setMessage(responseCheckSql.getMessage());

            UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(taskHistoryResult.getUser().getId(), taskHistoryResult.getTask().getId()).orElseThrow();

            userTask.setRights(true);

            userTaskRepository.save(userTask);

            updateResult(userTask);
        } else {
            taskHistoryResult.setRights(false);
            taskHistoryResult.setMessage(responseCheckSql.getMessage());
        }

        taskHistoryResultRepository.save(taskHistoryResult);
    }

    @RabbitListener(queues = "completed-code", group = "completed-code")
    @Transactional
    public void listenerCode(CodeExecuteResponse responseCheckCode) {

        log.info("Result check code");

        TaskHistoryResult taskHistoryResult = taskHistoryResultRepository.findById(responseCheckCode.getTaskId()).orElseThrow();

        if (responseCheckCode.getStatus().equals(Status.OK)) {
            taskRepository.updateRightAttempt(taskHistoryResult.getTask().getId());
            taskHistoryResult.setRights(true);
            taskHistoryResult.setMessage(responseCheckCode.getMessage());

            UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(taskHistoryResult.getUser().getId(), taskHistoryResult.getTask().getId()).orElseThrow();

            userTask.setRights(true);

            userTaskRepository.save(userTask);

            updateResult(userTask);
        } else {
            taskHistoryResult.setRights(false);
            taskHistoryResult.setMessage(responseCheckCode.getMessage());
        }

        taskHistoryResultRepository.save(taskHistoryResult);
    }


    private void updateResult(UserTask userTask) {
        CourseMarks courseMarks = courseMarksRepository.getCourseMarksByCourseIdAndCountTask(userTask.getUserTaskId().getTask().getCourses().getId(), userTask.getUserTaskId().getUser().getId());
        if (courseMarks != null) {
            UserCourse userCourse = userCourseRepository.getUserCourseByUserIdAndCourse(userTask.getUserTaskId().getTask().getCourses().getId(), userTask.getUserTaskId().getUser().getId()).orElseThrow(() -> new NotFoundException("Курс не найден"));

            userCourse.setCourseMarks(courseMarks);

            userCourseRepository.save(userCourse);
        }
    }
}
