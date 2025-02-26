package com.example.site.service.impl;

import com.example.site.dto.task_group.TaskGroupDto;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.TaskGroupMapper;
import com.example.site.model.TaskGroup;
import com.example.site.repository.TaskGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskGroupService {

    private final TaskGroupRepository taskGroupRepository;
    private final TaskGroupMapper taskGroupMapper;
    private final TaskServiceImpl taskService;

    @Transactional
    public void save(TaskGroupDto taskGroupDto) {
        taskGroupRepository.save(taskGroupMapper.dtoToEntity(taskGroupDto));
    }

    @Transactional(readOnly = true)
    public List<TaskGroupDto> getByCourseIdAndAdmin(Long courseId) {
        return taskGroupRepository.findByCourseId(courseId).stream().map(taskGroupMapper::entityToDto).toList();
    }

    @Transactional
    public void delete(Long taskGroupId) {
        TaskGroup taskGroup = taskGroupRepository.findById(taskGroupId).orElseThrow(() -> new NotFoundException("Task group not found"));

        taskGroup.setDeleted(true);

        taskGroupRepository.save(taskGroup);

        taskGroup.getTasks().forEach(item -> {
            taskService.deleteTask(item.getId());
        });
    }
}
