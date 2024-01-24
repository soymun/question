package com.example.site.workers;

import com.example.site.exception.NotFoundException;
import com.example.site.model.CourseMarks;
import com.example.site.model.TaskHistoryResult;
import com.example.site.model.UserCourse;
import com.example.site.model.UserTask;
import com.example.site.repository.CourseMarksRepository;
import com.example.site.repository.TaskHistoryResultRepository;
import com.example.site.repository.UserCourseRepository;
import com.example.site.repository.UserTaskRepository;
import dto.CodeExecuteResponse;
import dto.ResponseCheckSql;
import dto.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class Worker {

    private final TaskHistoryResultRepository taskHistoryResultRepository;

    private final CourseMarksRepository courseMarksRepository;

    private final UserCourseRepository userCourseRepository;

    private final UserTaskRepository userTaskRepository;


    @RabbitListener(queues = "result", group = "result")
    public void listener(ResponseCheckSql responseCheckSql) {

        TaskHistoryResult taskHistoryResult = taskHistoryResultRepository.findById(responseCheckSql.getTaskUserId()).orElseThrow();

        if (responseCheckSql.getStatus().equals(Status.OK)) {
            taskHistoryResult.setRights(true);
            taskHistoryResult.setMessage(responseCheckSql.getMessage());

            UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(taskHistoryResult.getTask().getId(), taskHistoryResult.getUser().getId()).orElseThrow();

            userTask.setRights(true);

            userTaskRepository.save(userTask);

            updateResult(userTask);
        } else {
            taskHistoryResult.setMessage(responseCheckSql.getMessage());
        }

        taskHistoryResultRepository.save(taskHistoryResult);
    }

    @RabbitListener(queues = "completed-code", group = "completed-code")
    public void listenerCode(CodeExecuteResponse responseCheckCode) {

        TaskHistoryResult taskHistoryResult = taskHistoryResultRepository.findById(responseCheckCode.getTaskId()).orElseThrow();

        if (responseCheckCode.getStatus().equals(Status.OK)) {
            taskHistoryResult.setRights(true);
            taskHistoryResult.setMessage(responseCheckCode.getMessage());

            UserTask userTask = userTaskRepository.getUserTaskByTaskIdAndUserId(taskHistoryResult.getTask().getId(), taskHistoryResult.getUser().getId()).orElseThrow();

            userTask.setRights(true);

            userTaskRepository.save(userTask);

            updateResult(userTask);
        } else {
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
