package com.example.site.service.impl;

import com.example.site.dto.RequestCreateSchema;
import com.example.site.dto.ResponseCreateSchema;
import com.example.site.dto.task.TaskAdminDto;
import com.example.site.dto.task.TaskDto;
import com.example.site.dto.task.TaskUserDto;
import com.example.site.dto.task.UserTaskDto;
import com.example.site.exception.ForbiddenException;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.TaskMapper;
import com.example.site.model.Courses;
import com.example.site.model.Task;
import com.example.site.model.UserTask;
import com.example.site.model.UserTaskId;
import com.example.site.model.util.TaskType;
import com.example.site.repository.*;
import com.example.site.security.UserDetailImpl;
import com.example.site.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskServiceImpl {

    private final TaskRepository taskRepository;

    private final CourseMarksRepository courseMarksRepository;

    private final UserCourseRepository userCourseRepository;

    private final UserTaskRepository userTaskRepository;

    private final RabbitTemplate rabbitTemplate;

    private final TaskMapper taskMapper;

    private final CourseRepository courseRepository;

    public TaskDto saveTask(TaskAdminDto taskAdminDto) {
        if (taskAdminDto != null) {

            log.info("Save task at course {}", taskAdminDto.getCourses());

            Task task;

            if (taskAdminDto.getId() == null) {
                task = taskMapper.createToEntity(taskAdminDto);
            } else {
                task = taskMapper.updateToEntity(taskAdminDto);
            }

            if (task.getTaskType() == TaskType.PostgreSQL || task.getTaskType() == TaskType.MySQL) {
                Optional<Courses> optionalCourses = courseRepository.getCoursesByTaskId(task.getCourses().getId());
                if (optionalCourses.isPresent()) {
                    Long countSql = courseRepository.getCourseSqlById(optionalCourses.get().getId(), List.of(task.getTaskType() == TaskType.PostgreSQL ? TaskType.MySQL : TaskType.PostgreSQL));
                    if (countSql > 0) {
                        throw new ForbiddenException("Невозможно создать 2 типа sql баз");
                    }
                }
            }

            Task savedTask = taskRepository.save(task);

            if (taskAdminDto.getId() == null) {
                Optional<Courses> optionalCourses = courseRepository.getCoursesByTaskId(task.getId());
                if (optionalCourses.isPresent()) {
                    Courses courses = optionalCourses.get();
                    if (courses.getSchema() == null || courses.getSchema().isEmpty()) {
                        courses.setSchema(((ResponseCreateSchema) Objects.requireNonNull(rabbitTemplate.convertSendAndReceiveAsType(task.getTaskType().getValue().toLowerCase() + "-schema", RequestCreateSchema.builder().courseId(courses.getId()).build(), new ParameterizedTypeReference<>() {
                            @NotNull
                            @Override
                            public Type getType() {
                                return ResponseCreateSchema.class;
                            }
                        }))).getSchema());
                        courses.setSqlType(task.getTaskType());
                        courseRepository.save(courses);
                    }
                }
            }

            userTaskRepository.saveAll(userCourseRepository.getUserCourseByCourseId(taskAdminDto.getCourses()).stream().map(uc -> new UserTask(new UserTaskId(uc.getUserCourseId().getUser().getId(), savedTask.getId()), false, false, 0L)).toList());

            return taskMapper.taskToTaskDto(savedTask);
        } else {
            throw new IllegalArgumentException("Невозможно создать задачу");
        }
    }

    public void deleteTask(Long id) {

        log.info("Delete task {}", id);

        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Не найдена задача"));
        task.setDeleted(true);
        taskRepository.save(task);
        userTaskRepository.deleteUserTaskByTaskId(id);
        userCourseRepository.saveAll(userCourseRepository.getUserCourseByCourseId(task.getCourses().getId()).stream().peek(uc -> uc.setCourseMarks(courseMarksRepository.getCourseMarksLessCountByCourseId(task.getCourses().getId()))).toList());
    }


    @Deprecated
    public List<TaskDto> getAllByCourseId(Long id) {
        return taskRepository.findAllByCourseId(id).stream().map(taskMapper::taskToTaskDto).toList();
    }

    public TaskAdminDto getByIdTeacher(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            return taskMapper.taskToTaskAdminDtoDto(task);
        }

        throw new NotFoundException("Не найдена задача");
    }

    public TaskUserDto getToUser(Long id) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        Optional<Task> optionalTask = userTaskRepository.getUserTaskByUserIdAndTaskId(userDetail.getId(), id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            return taskMapper.taskToUserDto(task);
        }

        throw new NotFoundException("Не найдена задача");
    }


    @Deprecated
    public List<UserTaskDto> getTaskToUserByCourse(Long courseId) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        return userTaskRepository.getUserTaskByUserIdAndCourseIdToGet(userDetail.getId(), courseId, userDetail.isAdmin())
                .stream()
                .map(taskMapper::userTaskToUserTaskDto)
                .collect(Collectors.toList());
    }

    public List<UserTaskDto> getTaskToUserByCourseAndTaskGroup(Long courseId, Long taskGroupId) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        return userTaskRepository.getUserTaskByUserIdAndCourseIdToGetAndTaskGroup(userDetail.getId(), courseId, userDetail.isAdmin(), taskGroupId)
                .stream()
                .map(taskMapper::userTaskToUserTaskDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getAllByCourseIdAndTaskGroup(Long id, Long taskGroupId) {
        return taskRepository.findAllByCourseIdAndTaskGroup(id, taskGroupId).stream().map(taskMapper::taskToTaskDto).toList();
    }
}
