package com.example.site.controllers;

import com.example.site.dto.ResultDto;
import com.example.site.dto.task.*;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.impl.ExecuteServiceImpl;
import com.example.site.service.impl.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(description = "Сохранение задачи")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskDto>> saveTask(@Valid @RequestBody TaskAdminDto taskAdminDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.saveTask(taskAdminDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Удаление задачи")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskDto>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/course/{id}")
    @Operation(description = "Получить все задачи")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<TaskDto>>> getAllByCourseId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getAllByCourseId(id)));
    }

    @GetMapping("/user/course/{id}")
    @Operation(description = "Получить для пользователя")
    public ResponseEntity<ResultDto<List<UserTaskDto>>> getAllByQuery(@PathVariable Long id, @AuthenticationPrincipal UserDetailImpl principal, @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getTaskToUserByCourse(principal.getId(), id, grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }

    @GetMapping("/user/get/{id}")
    @Operation(description = "Получить для пользователя")
    public ResponseEntity<ResultDto<TaskUserDto>> getToUser(@PathVariable Long id, @AuthenticationPrincipal(expression = "id") Long user) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getToUser(id, user)));
    }

    @GetMapping("/teacher/get/{id}")
    @Operation(description = "Получить для учителя")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskAdminDto>> getAllByQuery(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getByIdTeacher(id)));
    }


    @PostMapping("/execute")
    @Operation(description = "Выполнить задачу")
    public ResponseEntity<ResultDto<?>> executeSql(@RequestBody ExecuteDto executeDto, @AuthenticationPrincipal(expression = "id") Long id) {
        return ResponseEntity.ok(new ResultDto<>(executeService.execute(executeDto, id)));
    }
}
