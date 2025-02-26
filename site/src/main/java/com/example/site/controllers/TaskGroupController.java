package com.example.site.controllers;

import com.example.site.dto.ResultDto;
import com.example.site.dto.task.TaskDto;
import com.example.site.dto.task_group.TaskGroupDto;
import com.example.site.service.impl.TaskGroupService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task-group")
public class TaskGroupController {

    private final TaskGroupService taskGroupService;

    @PostMapping
    @Schema(description = "Сохранение задачи")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskDto>> save(@Valid @RequestBody TaskGroupDto taskGroupDto) {
        taskGroupService.save(taskGroupDto);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}")
    @Schema(description = "Удаление задачи")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskDto>> delete(@PathVariable Long id) {
        taskGroupService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/search/course/{id}")
    @Schema(description = "Получить все задачи")
    public ResponseEntity<ResultDto<List<TaskGroupDto>>> getAllByCourseId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskGroupService.getByCourseIdAndAdmin(id)));
    }
}
