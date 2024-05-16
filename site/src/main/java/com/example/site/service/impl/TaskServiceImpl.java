package com.example.site.service.impl;

import com.example.site.dto.task.*;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.TaskMapper;
import com.example.site.model.*;
import com.example.site.repository.*;
import com.example.site.service.TaskService;
import dto.RequestCreateSchema;
import dto.ResponseCreateSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

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

    @Override
    public TaskDto createTask(TaskCreateDto taskCreateDto) {
        if (taskCreateDto != null) {

            log.info("Save task at course {}", taskCreateDto.getCourses());

            Task task = taskMapper.taskCreateDtoToTask(taskCreateDto);
            task.setDeleted(false);
            task.setOpen(false);
            Task savedTask = taskRepository.save(task);

            userTaskRepository.saveAll(userCourseRepository.getUserCourseByCourseId(taskCreateDto.getCourses()).stream().map(uc -> new UserTask(new UserTaskId(uc.getUserCourseId().getUser().getId(), savedTask.getId()), false, false, 1L)).toList());

            return taskMapper.taskToTaskDto(savedTask);
        } else {
            throw new IllegalArgumentException("Невозможно создать задачу");
        }
    }

    @Override
    public void deleteTask(Long id) {

        log.info("Delete task {}", id);

        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Не найдена задача"));
        task.setDeleted(true);
        taskRepository.save(task);
        userTaskRepository.deleteUserTaskByTaskId(id);
        userCourseRepository.saveAll(userCourseRepository.getUserCourseByCourseId(task.getCourses().getId()).stream().peek(uc -> uc.setCourseMarks(courseMarksRepository.getCourseMarksLessCountByCourseId(task.getCourses().getId()))).toList());
    }

    @Override
    public TaskDto updateDto(TaskUpdateDto taskUpdateDto) {
        if (taskUpdateDto != null) {

            log.info("Update task {}", taskUpdateDto.getId());

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
    public List<UserTaskDto> getTaskToUserByCourse(Long userId, Long courseId) {
        return userTaskRepository.getUserTaskByUserIdAndCourseId(userId, courseId).stream().map(taskMapper::userTaskToUserTaskDto).collect(Collectors.toList());
    }

    @Override
    public TaskInfoCodeDtoAdmin createTaskInfoCode(TaskInfoCodeCreateDto taskInfoCodeCreateDto) {
        if (taskInfoCodeCreateDto != null) {

            log.info("Create code info");

            TaskInfoCode taskInfoCode = taskMapper.taskInfoCodeCreateToTaskInfoCode(taskInfoCodeCreateDto);
            return taskMapper.taskInfoCodeToTaskInfoCodeDtoAdmin(taskInfoCodeRepository.save(taskInfoCode));
        } else {
            throw new IllegalArgumentException("Невозможно создать задачу");
        }
    }

    @Override
    public TaskInfoCodeDtoAdmin updateTaskInfoCode(TaskInfoCodeUpdateDto taskInfoCodeUpdateDto) {
        if (taskInfoCodeUpdateDto != null) {

            log.info("Update code info {}", taskInfoCodeUpdateDto.getId());

            TaskInfoCode taskInfoCode = taskInfoCodeRepository.findById(taskInfoCodeUpdateDto.getId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            ofNullable(taskInfoCodeUpdateDto.getUserClass()).ifPresent(taskInfoCode::setUserClass);
            ofNullable(taskInfoCodeUpdateDto.getCheckCode()).ifPresent(taskInfoCode::setCheckCode);
            ofNullable(taskInfoCodeUpdateDto.getInitCode()).ifPresent(taskInfoCode::setInitCode);
            ofNullable(taskInfoCodeUpdateDto.getCheckClass()).ifPresent(taskInfoCode::setCheckClass);

            return taskMapper.taskInfoCodeToTaskInfoCodeDtoAdmin(taskInfoCodeRepository.save(taskInfoCode));
        } else {
            throw new IllegalArgumentException("Невозможно создать задачу");
        }
    }

    @Override
    public List<TaskInfoCodeDtoAdmin> getInfoCodeByTaskIdAdmin(Long id) {
        return taskInfoCodeRepository.getTaskInfoCodesByTaskId(id).stream().map(taskMapper::taskInfoCodeToTaskInfoCodeDtoAdmin).toList();
    }

    @Override
    public List<TaskInfoCodeDto> getInfoCodeByTaskId(Long id) {
        return taskInfoCodeRepository.getTaskInfoCodesByTaskId(id).stream().map(taskMapper::taskInfoCodeToTaskInfoCodeDto).toList();
    }

    @Override
    public void deleteTaskInfoCode(Long id) {

        log.info("Delete info code {}", id);

        taskInfoCodeRepository.deleteById(id);
    }

    @Override
    public TaskInfoQuestionBoxAdminDto createTaskInfoBox(TaskInfoQuestionBoxCreateDto taskInfoQuestionBoxCreateDto) {
        if (taskInfoQuestionBoxCreateDto != null) {

            log.info("Create task info box");

            return taskMapper.taskInfoBoxToAdminDto(taskInfoQuestionBoxRepository.save(taskMapper.taskInfoBoxCreateDtoToTaskInfoBox(taskInfoQuestionBoxCreateDto)));
        } else {
            throw new IllegalArgumentException("Невозможно создать задачу");
        }
    }

    @Override
    public TaskInfoQuestionBoxAdminDto updateTaskInfoBox(TaskInfoQuestionBoxUpdateDto taskInfoQuestionBoxUpdateDto) {

        if (taskInfoQuestionBoxUpdateDto != null) {

            log.info("Update task info box");

            TaskInfoQuestionBox taskInfoQuestionBox = taskInfoQuestionBoxRepository.findById(taskInfoQuestionBoxUpdateDto.getId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            ofNullable(taskInfoQuestionBoxUpdateDto.getAnswer()).ifPresent(taskInfoQuestionBox::setAnswer);
            ofNullable(taskInfoQuestionBoxUpdateDto.getRights()).ifPresent(taskInfoQuestionBox::setRights);

            return taskMapper.taskInfoBoxToAdminDto(taskInfoQuestionBoxRepository.save(taskInfoQuestionBox));

        } else {
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
    public void deleteTaskInfoBox(Long id) {

        log.info("Delete info box {}", id);

        taskInfoQuestionBoxRepository.deleteById(id);
    }

    @Override
    public TaskInfoQuestionTextDto createTaskInfoText(TaskInfoQuestionTextCreateDto taskInfoQuestionTextCreateDto) {
        try {
            if (taskInfoQuestionTextCreateDto != null) {

                log.info("Create info text");

                TaskInfoQuestionText taskInfoQuestionText = taskMapper.taskInfoTextCreateDtoToTaskInfoText(taskInfoQuestionTextCreateDto);
                return taskMapper.taskInfoTextToTaskInfoTextDto(taskInfoQuestionTextRepository.save(taskInfoQuestionText));
            } else {
                throw new IllegalArgumentException("Невозможно создать задачу");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public TaskInfoQuestionTextDto updateTaskInfoText(TaskInfoQuestionTextUpdateDto taskInfoQuestionTextCreateDto) {
        if (taskInfoQuestionTextCreateDto != null) {

            log.info("Update info text {}", taskInfoQuestionTextCreateDto.getId());

            TaskInfoQuestionText taskInfoQuestionText = taskInfoQuestionTextRepository.findById(taskInfoQuestionTextCreateDto.getId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            ofNullable(taskInfoQuestionTextCreateDto.getAnswer()).ifPresent(taskInfoQuestionText::setAnswer);

            return taskMapper.taskInfoTextToTaskInfoTextDto(taskInfoQuestionTextRepository.save(taskInfoQuestionText));

        } else {
            throw new IllegalArgumentException("Невозможно создать задачу");
        }
    }

    @Override
    public TaskInfoQuestionTextDto getTextByTaskId(Long taskId) {
        return taskMapper.taskInfoTextToTaskInfoTextDto(taskInfoQuestionTextRepository.findByTaskId(taskId).orElseThrow(() -> new NotFoundException("Задача не найдена")));
    }

    @Override
    public void deleteTaskInfoText(Long id) {

        log.info("Delete info text {}", id);

        taskInfoQuestionTextRepository.deleteById(id);
    }

    @Override
    public TaskInfoSqlAdminDto createTaskInfoSql(TaskInfoSqlCreateDto taskInfoSqlCreateDto) {
        try {
            if (taskInfoSqlCreateDto != null) {

                log.info("Create info sql");

                TaskInfoSql taskInfoSql = taskMapper.taskInfoCreateSqlToTaskInfoSql(taskInfoSqlCreateDto);

                Optional<Courses> optionalCourses = courseRepository.getCoursesByTaskId(taskInfoSqlCreateDto.getTask());
                if (optionalCourses.isPresent()) {
                    Courses courses = optionalCourses.get();
                    if (courses.getSchema() == null || courses.getSchema().isEmpty()) {
                        courses.setSchema(((ResponseCreateSchema) Objects.requireNonNull(rabbitTemplate.convertSendAndReceive("schema", RequestCreateSchema.builder().courseId(courses.getId()).build()))).getSchema());
                        courseRepository.save(courses);
                    }
                }

                return taskMapper.taskSqlToDtoAdmin(taskInfoSqlRepository.save(taskInfoSql));
            } else {
                throw new IllegalArgumentException("Невозможно создать задачу");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public TaskInfoSqlAdminDto updateTaskInfoSql(TaskInfoSqlUpdateDto taskInfoSqlCreateDto) {
        if (taskInfoSqlCreateDto != null) {

            log.info("Update info sql {}", taskInfoSqlCreateDto.getId());

            TaskInfoSql taskInfoSql = taskInfoSqlRepository.findById(taskInfoSqlCreateDto.getId()).orElseThrow(() -> new NotFoundException("Задача не найдена"));

            ofNullable(taskInfoSqlCreateDto.getCheckSql()).ifPresent(taskInfoSql::setCheckSql);
            ofNullable(taskInfoSqlCreateDto.getMainSql()).ifPresent(taskInfoSql::setMainSql);

            return taskMapper.taskSqlToDtoAdmin(taskInfoSqlRepository.save(taskInfoSql));
        } else {
            throw new IllegalArgumentException("Невозможно создать задачу");
        }
    }

    @Override
    public void deleteTaskInfoSql(Long id) {

        log.info("Delete info sql {}", id);

        taskInfoSqlRepository.deleteById(id);
    }

    @Override
    public TaskInfoSqlAdminDto getSqlAdminByTaskId(Long id) {
        return taskMapper.taskSqlToDtoAdmin(taskInfoSqlRepository.getByTaskId(id).orElseThrow(() -> new NotFoundException("Задача не найдена")));
    }
}
