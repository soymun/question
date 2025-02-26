package com.example.site.mappers;


import com.example.site.dto.task.*;
import com.example.site.exception.NotFoundException;
import com.example.site.model.*;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {

    @Autowired
    public EntityManager entityManager;

    public Task createToEntity(TaskAdminDto taskAdminDto) {
        Task task;
        if (taskAdminDto.getId() == null) {
            task = new Task();
        } else {
            task = entityManager.getReference(Task.class, taskAdminDto.getId());
            if (task == null) {
                throw new NotFoundException("Task not found");
            }
        }

        taskCreateDtoToTask(taskAdminDto, task);
        return task;
    }

    public Task updateToEntity(TaskAdminDto taskAdminDto) {
        Task task;
        if (taskAdminDto.getId() == null) {
            task = new Task();
        } else {
            task = entityManager.getReference(Task.class, taskAdminDto.getId());
            if (task == null) {
                throw new NotFoundException("Task not found");
            }
        }

        taskCreateDtoToTask(taskAdminDto, task);

        switch (task.getTaskType()) {
            case PostgreSQL, MySQL -> {
                if (taskAdminDto.getTaskInfoSql() == null) {
                    throw new NotFoundException("TaskInfoSql not found");
                }
                TaskInfoSql taskInfoSql = task.getTaskInfoSql();
                taskInfoSql.setTask(task);
            }
            case CODE -> {
                if (taskAdminDto.getTaskInfoCode() == null) {
                    throw new NotFoundException("TaskInfoCode not found");
                }
                adminToEntityCode(taskAdminDto, task);
            }
            case QUESTION_TEXT -> {
                if (taskAdminDto.getTaskInfoQuestionText() == null) {
                    throw new NotFoundException("TaskInfoQuestionText not found");
                }
                TaskInfoQuestionText taskInfoQuestionText = task.getTaskInfoQuestionText();
                taskInfoQuestionText.setTask(task);
            }
            case QUESTION_BOX_ONE, QUESTION_BOX_MULTI -> {
                if (taskAdminDto.getTaskInfoQuestionBox() == null) {
                    throw new NotFoundException("TaskInfoQuestionBox not found");
                }

                adminToEntityBox(taskAdminDto, task);
            }
        }

        return task;
    }

    private void adminToEntityBox(TaskAdminDto taskAdminDto, Task task) {
        List<TaskInfoQuestionBox> taskInfoQuestionBoxes = task.getTaskInfoQuestionBox();

        if (taskInfoQuestionBoxes == null) {
            taskInfoQuestionBoxes = new ArrayList<>();
            task.setTaskInfoQuestionBox(taskInfoQuestionBoxes);
        }

        List<TaskInfoQuestionBoxAdminDto> withIds = taskAdminDto.getTaskInfoQuestionBox().stream().filter(x -> x.getId() != null).toList();
        List<TaskInfoQuestionBoxAdminDto> nullIds = taskAdminDto.getTaskInfoQuestionBox().stream().filter(x -> x.getId() == null).toList();

        Map<Long, TaskInfoQuestionBox> map = taskInfoQuestionBoxes.stream().collect(Collectors.toMap(TaskInfoQuestionBox::getId, x -> x));

        for (TaskInfoQuestionBoxAdminDto taskInfoQuestionBoxAdminDto : withIds) {
            if (map.containsKey(taskInfoQuestionBoxAdminDto.getId())) {
                TaskInfoQuestionBox taskInfoQuestionBox = map.get(taskInfoQuestionBoxAdminDto.getId());
                taskBoxToEntity(taskInfoQuestionBoxAdminDto, taskInfoQuestionBox);
                taskInfoQuestionBox.setTask(task);
                map.remove(taskInfoQuestionBoxAdminDto.getId());
            }
        }

        taskInfoQuestionBoxes.removeAll(map.values());

        for (TaskInfoQuestionBoxAdminDto taskInfoQuestionBoxAdminDto : nullIds) {
            TaskInfoQuestionBox taskInfoQuestionBox = new TaskInfoQuestionBox();
            taskBoxToEntity(taskInfoQuestionBoxAdminDto, taskInfoQuestionBox);
            taskInfoQuestionBox.setTask(task);
            taskInfoQuestionBoxes.add(taskInfoQuestionBox);
        }
    }

    private void adminToEntityCode(TaskAdminDto taskAdminDto, Task task) {
        List<TaskInfoCode> taskInfoQuestionBoxes = task.getTaskInfoCode();

        if (taskInfoQuestionBoxes == null) {
            taskInfoQuestionBoxes = new ArrayList<>();
            task.setTaskInfoCode(taskInfoQuestionBoxes);
        }

        List<TaskInfoCodeAdminDto> withIds = taskAdminDto.getTaskInfoCode().stream().filter(x -> x.getId() != null).toList();
        List<TaskInfoCodeAdminDto> nullIds = taskAdminDto.getTaskInfoCode().stream().filter(x -> x.getId() == null).toList();

        Map<Long, TaskInfoCode> map = taskInfoQuestionBoxes.stream().collect(Collectors.toMap(TaskInfoCode::getId, x -> x));

        for (TaskInfoCodeAdminDto taskInfoQuestionBoxAdminDto : withIds) {
            if (map.containsKey(taskInfoQuestionBoxAdminDto.getId())) {
                TaskInfoCode taskInfoQuestionBox = map.get(taskInfoQuestionBoxAdminDto.getId());
                taskCodeToEntity(taskInfoQuestionBoxAdminDto, taskInfoQuestionBox);
                taskInfoQuestionBox.setTask(task);
                map.remove(taskInfoQuestionBoxAdminDto.getId());
            }
        }

        taskInfoQuestionBoxes.removeAll(map.values());

        for (TaskInfoCodeAdminDto taskInfoQuestionBoxAdminDto : nullIds) {
            TaskInfoCode taskInfoQuestionBox = new TaskInfoCode();
            taskCodeToEntity(taskInfoQuestionBoxAdminDto, taskInfoQuestionBox);
            taskInfoQuestionBox.setTask(task);
            taskInfoQuestionBoxes.add(taskInfoQuestionBox);
        }
    }

    @Mapping(target = "taskInfoQuestionBox", ignore = true)
    @Mapping(target = "taskInfoCode", ignore = true)
    public abstract void taskCreateDtoToTask(TaskAdminDto taskAdminDto, @MappingTarget Task task);

    public abstract void taskBoxToEntity(TaskInfoQuestionBoxAdminDto taskAdminDto, @MappingTarget TaskInfoQuestionBox task);

    public abstract void taskCodeToEntity(TaskInfoCodeAdminDto taskAdminDto, @MappingTarget TaskInfoCode task);


    public abstract TaskDto taskToTaskDto(Task task);

    public abstract TaskAdminDto taskToTaskAdminDtoDto(Task task);

    public abstract TaskUserDto taskToUserDto(Task task);

    public abstract UserTaskDto userTaskToUserTaskDto(UserTask userTask);

    public abstract ResultExecute taskHistoryResultToResultExecute(TaskHistoryResult taskHistoryResult);


    public Courses map(Long id) {
        return new Courses(id);
    }

    public Long map1(Courses courses) {
        if (courses != null) {
            return courses.getId();
        }
        return null;
    }

    public TaskGroup mapTaskGroup(Long id) {
        return new TaskGroup(id);
    }

    public Long taskGroupToLong(TaskGroup courses) {
        if (courses != null) {
            return courses.getId();
        }
        return null;
    }

    public Task map2(Long id) {
        return new Task(id);
    }

    public Task map3(UserTaskId userTaskId) {
        return userTaskId.getTask();
    }

    public Long map4(Task task) {
        if (task == null) return null;
        return task.getId();
    }

}
