package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.task.*;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.ExecuteService;
import com.example.site.service.impl.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Task Controller")
@RequestMapping("/task")
public class TaskController {

    private final TaskServiceImpl taskService;

    private final ExecuteService executeService;

    @PostMapping
    @Operation(description = "Создание задачи")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskDto>> saveTask(@Valid @RequestBody TaskCreateDto taskCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.createTask(taskCreateDto)));
    }

    @PutMapping
    @Operation(description = "Изменение задачи")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskDto>> updateTask(@Valid @RequestBody TaskUpdateDto taskUpdateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.updateDto(taskUpdateDto)));
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
    public ResponseEntity<ResultDto<List<UserTaskDto>>> getAllByQuery(@PathVariable Long id, @AuthenticationPrincipal UserDetailImpl principal) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getTaskToUserByCourse(principal.getId(), id)));
    }

    @PostMapping("/info/code")
    @Operation(description = "Создать информация для задачи по коду")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoCodeDtoAdmin>> createTaskInfoCode(@Valid @RequestBody TaskInfoCodeCreateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.createTaskInfoCode(taskInfoCodeCreateDto)));
    }

    @PutMapping ("/info/code")
    @Operation(description = "Изменить информация для задачи по коду")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoCodeDtoAdmin>> updateTaskInfoCode(@Valid @RequestBody TaskInfoCodeUpdateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.updateTaskInfoCode(taskInfoCodeCreateDto)));
    }

    @GetMapping ("/info/code/task/{id}/admin")
    @Operation(description = "Получить информация для задачи по коду для учителя")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<TaskInfoCodeDtoAdmin>>> getTaskInfoCodeByTaskIdAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getInfoCodeByTaskIdAdmin(id)));
    }

    @GetMapping ("/info/code/task/{id}")
    @Operation(description = "Получить информация для задачи по коду")
    public ResponseEntity<ResultDto<List<TaskInfoCodeDto>>> getTaskInfoCodeByTaskId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getInfoCodeByTaskId(id)));
    }

    @PostMapping("/info/code/execute")
    @Operation(description = "Выполнить задачу коду")
    public ResponseEntity<ResultDto<?>> executeCode(@RequestBody ExecuteCodeDto executeCodeDto, @AuthenticationPrincipal(expression = "id") Long id) {
        executeService.executeCode(executeCodeDto, id);
        return ResponseEntity.ok(new ResultDto<>());
    }

    @DeleteMapping ("/info/code/{id}")
    @Operation(description = "Удалить информация для задачи по коду")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<TaskInfoCodeDtoAdmin>>> delete(@PathVariable Long id) {
        taskService.deleteTaskInfoCode(id);
        return ResponseEntity.ok(new ResultDto<>());
    }


    @PostMapping("/info/box")
    @Operation(description = "Создать информация для задачи по выбору")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoQuestionBoxAdminDto>> createTaskInfoBox(@Valid @RequestBody TaskInfoQuestionBoxCreateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.createTaskInfoBox(taskInfoCodeCreateDto)));
    }

    @PutMapping ("/info/box")
    @Operation(description = "Изменить информация для задачи по выбору")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoQuestionBoxAdminDto>> updateTaskInfoBox(@Valid @RequestBody TaskInfoQuestionBoxUpdateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.updateTaskInfoBox(taskInfoCodeCreateDto)));
    }

    @GetMapping ("/info/box/task/{id}/admin")
    @Operation(description = "Получить информация для задачи по выбору для учителя")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<TaskInfoQuestionBoxAdminDto>>> getTaskInfoBoxByTaskIdAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getByTaskIdAdmin(id)));
    }

    @GetMapping ("/info/box/task/{id}")
    @Operation(description = "Получить информация для задачи по выбору")
    public ResponseEntity<ResultDto<List<TaskInfoQuestionBoxDto>>> getTaskInfoBoxByTaskId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getByTaskId(id)));
    }

    @PostMapping("/info/box/execute")
    @Operation(description = "Выполнить информация для задачи по выбору")
    public ResponseEntity<ResultDto<ResultExecute>> executeBox(@RequestBody ExecuteBoxDto executeCodeDto, @AuthenticationPrincipal(expression = "id") Long id) {
        return ResponseEntity.ok(new ResultDto<>(executeService.executeBox(executeCodeDto, id)));
    }

    @DeleteMapping ("/info/box/{id}")
    @Operation(description = "Удалить информация для задачи по выбору")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<TaskInfoCodeDtoAdmin>>> deleteBox(@PathVariable Long id) {
        taskService.deleteTaskInfoBox(id);
        return ResponseEntity.ok(new ResultDto<>());
    }


    @PostMapping("/info/text")
    @Operation(description = "Создать информация для задачи по тексту")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoQuestionTextDto>> createTaskInfoText(@Valid @RequestBody TaskInfoQuestionTextCreateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.createTaskInfoText(taskInfoCodeCreateDto)));
    }

    @PutMapping ("/info/text")
    @Operation(description = "Изменить информация для задачи по тексту")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoQuestionTextDto>> updateTaskInfoText(@Valid @RequestBody TaskInfoQuestionTextUpdateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.updateTaskInfoText(taskInfoCodeCreateDto)));
    }

    @GetMapping ("/info/text/task/{id}")
    @Operation(description = "Получить информация для задачи по тексту")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoQuestionTextDto>> getTaskInfoTextByTaskId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getTextByTaskId(id)));
    }

    @PostMapping("/info/text/execute")
    @Operation(description = "Выполнить информация для задачи по тексту")
    public ResponseEntity<ResultDto<ResultExecute>> executeText(@RequestBody ExecuteTextDto executeCodeDto, @AuthenticationPrincipal(expression = "id") Long id) {
        return ResponseEntity.ok(new ResultDto<>(executeService.executeText(executeCodeDto, id)));
    }

    @DeleteMapping ("/info/text/{id}")
    @Operation(description = "Удалить информация для задачи по тексту")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<?>> deleteText(@PathVariable Long id) {
        taskService.deleteTaskInfoText(id);
        return ResponseEntity.ok(new ResultDto<>());
    }


    @PostMapping("/info/sql")
    @Operation(description = "Создать информация для задачи по sql")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoSqlAdminDto>> createTaskInfoSql(@Valid @RequestBody TaskInfoSqlCreateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.createTaskInfoSql(taskInfoCodeCreateDto)));
    }

    @PutMapping ("/info/sql")
    @Operation(description = "Изменить информация для задачи по sql")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoSqlAdminDto>> updateTaskInfoSql(@Valid @RequestBody TaskInfoSqlUpdateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.updateTaskInfoSql(taskInfoCodeCreateDto)));
    }

    @GetMapping ("/info/sql/task/{id}")
    @Operation(description = "Получить информация для задачи по sql")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoSqlAdminDto>> getTaskInfoSqlByTaskId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getSqlAdminByTaskId(id)));
    }

    @PostMapping("/info/sql/execute")
    @Operation(description = "Выполнить информация для задачи по sql")
    public ResponseEntity<ResultDto<?>> executeSql(@RequestBody ExecuteSqlDto executeCodeDto, @AuthenticationPrincipal(expression = "id") Long id) {
        executeService.executeSql(executeCodeDto, id);
        return ResponseEntity.ok(new ResultDto<>());
    }

    @DeleteMapping ("/info/sql/{id}")
    @Operation(description = "Удалить информация для задачи по sql")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<?>> deleteSql(@PathVariable Long id) {
        taskService.deleteTaskInfoSql(id);
        return ResponseEntity.ok(new ResultDto<>());
    }
}
