package com.example.site.controllers;

import com.example.site.dto.ResultDto;
import com.example.site.dto.task.*;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.impl.ExecuteServiceImpl;
import com.example.site.service.impl.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Task Controller")
@RequestMapping("/task")
public class TaskController {

    private final TaskServiceImpl taskService;

    private final ExecuteServiceImpl executeService;

    @PostMapping
    @Schema(description = "Сохранение задачи")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskDto>> saveTask(@Valid @RequestBody TaskAdminDto taskAdminDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.saveTask(taskAdminDto)));
    }

    @DeleteMapping("/{id}")
    @Schema(description = "Удаление задачи")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskDto>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/search/course/{id}")
    @Schema(description = "Получить все задачи")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<TaskDto>>> getAllByCourseId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getAllByCourseId(id)));
    }

    @GetMapping("/search/user/course/{id}")
    @Schema(description = "Получить все задачи для пользователя по курсу")
    public ResponseEntity<ResultDto<List<UserTaskDto>>> getAllByQueryUser(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getTaskToUserByCourse(id)));
    }

    @GetMapping("/{id}")
    @Schema(description = "Получить для пользователя")
    public ResponseEntity<ResultDto<TaskUserDto>> getToUser(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getToUser(id)));
    }

    @GetMapping("/teacher/{id}")
    @Schema(description = "Получить для учителя")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskAdminDto>> getAllByQueryTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getByIdTeacher(id)));
    }

    @PostMapping("/execute")
    @Schema(description = "Выполнить задачу")
    public ResponseEntity<ResultDto<?>> executeSql(@RequestBody ExecuteDto executeDto) {
        return ResponseEntity.ok(new ResultDto<>(executeService.execute(executeDto)));
    }
}
