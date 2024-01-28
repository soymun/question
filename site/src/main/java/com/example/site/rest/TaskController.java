package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.task.*;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.impl.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskServiceImpl taskService;


    @PostMapping
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskDto>> saveTask(@RequestBody TaskCreateDto taskCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.createTask(taskCreateDto)));
    }

    @PutMapping
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskDto>> updateTask(@RequestBody TaskUpdateDto taskUpdateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.updateDto(taskUpdateDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskDto>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/course/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<TaskDto>>> getAllByCourseId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getAllByCourseId(id)));
    }

    @GetMapping("/user/course/{id}")
    public ResponseEntity<ResultDto<List<UserTaskDto>>> getAllByQuery(@PathVariable Long id, @AuthenticationPrincipal UserDetailImpl principal) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getTaskToUserByCourse(principal.getId(), id)));
    }

    @PostMapping("/info/code")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoCodeDtoAdmin>> createTaskInfoCode(@RequestBody TaskInfoCodeCreateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.createTaskInfoCode(taskInfoCodeCreateDto)));
    }

    @PutMapping ("/info/code")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoCodeDtoAdmin>> updateTaskInfoCode(@RequestBody TaskInfoCodeUpdateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.updateTaskInfoCode(taskInfoCodeCreateDto)));
    }

    @GetMapping ("/info/code/task/{id}/admin")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<TaskInfoCodeDtoAdmin>>> getTaskInfoCodeByTaskIdAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getInfoCodeByTaskIdAdmin(id)));
    }

    @GetMapping ("/info/code/task/{id}")
    public ResponseEntity<ResultDto<List<TaskInfoCodeDto>>> getTaskInfoCodeByTaskId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getInfoCodeByTaskId(id)));
    }

    @PostMapping("/info/code/execute")
    public ResponseEntity<ResultDto<?>> executeCode(@RequestBody ExecuteCodeDto executeCodeDto, @AuthenticationPrincipal(expression = "id") Long id) {
        taskService.executeCode(executeCodeDto, id);
        return ResponseEntity.ok(new ResultDto<>());
    }

    @DeleteMapping ("/info/code/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<TaskInfoCodeDtoAdmin>>> delete(@PathVariable Long id) {
        taskService.deleteTaskInfoCode(id);
        return ResponseEntity.ok(new ResultDto<>());
    }


    @PostMapping("/info/box")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoQuestionBoxAdminDto>> createTaskInfoBox(@RequestBody TaskInfoQuestionBoxCreateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.createTaskInfoBox(taskInfoCodeCreateDto)));
    }

    @PutMapping ("/info/box")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoQuestionBoxAdminDto>> updateTaskInfoBox(@RequestBody TaskInfoQuestionBoxUpdateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.updateTaskInfoBox(taskInfoCodeCreateDto)));
    }

    @GetMapping ("/info/box/task/{id}/admin")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<TaskInfoQuestionBoxAdminDto>>> getTaskInfoBoxByTaskIdAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getByTaskIdAdmin(id)));
    }

    @GetMapping ("/info/box/task/{id}")
    public ResponseEntity<ResultDto<List<TaskInfoQuestionBoxDto>>> getTaskInfoBoxByTaskId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getByTaskId(id)));
    }

    @PostMapping("/info/box/execute")
    public ResponseEntity<ResultDto<ResultExecute>> executeBox(@RequestBody ExecuteBoxDto executeCodeDto, @AuthenticationPrincipal(expression = "id") Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.executeBox(executeCodeDto, id)));
    }

    @DeleteMapping ("/info/box/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<TaskInfoCodeDtoAdmin>>> deleteBox(@PathVariable Long id) {
        taskService.deleteTaskInfoBox(id);
        return ResponseEntity.ok(new ResultDto<>());
    }


    @PostMapping("/info/text")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoQuestionTextDto>> createTaskInfoText(@RequestBody TaskInfoQuestionTextCreateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.createTaskInfoText(taskInfoCodeCreateDto)));
    }

    @PutMapping ("/info/text")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoQuestionTextDto>> updateTaskInfoText(@RequestBody TaskInfoQuestionTextUpdateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.updateTaskInfoText(taskInfoCodeCreateDto)));
    }

    @GetMapping ("/info/text/task/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoQuestionTextDto>> getTaskInfoTextByTaskId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getTextByTaskId(id)));
    }

    @PostMapping("/info/text/execute")
    public ResponseEntity<ResultDto<ResultExecute>> executeText(@RequestBody ExecuteTextDto executeCodeDto, @AuthenticationPrincipal(expression = "id") Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.executeText(executeCodeDto, id)));
    }

    @DeleteMapping ("/info/text/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<?>> deleteText(@PathVariable Long id) {
        taskService.deleteTaskInfoText(id);
        return ResponseEntity.ok(new ResultDto<>());
    }


    @PostMapping("/info/sql")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoSqlAdminDto>> createTaskInfoSql(@RequestBody TaskInfoSqlCreateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.createTaskInfoSql(taskInfoCodeCreateDto)));
    }

    @PutMapping ("/info/sql")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoSqlAdminDto>> updateTaskInfoSql(@RequestBody TaskInfoSqlUpdateDto taskInfoCodeCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(taskService.updateTaskInfoSql(taskInfoCodeCreateDto)));
    }

    @GetMapping ("/info/sql/task/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<TaskInfoSqlAdminDto>> getTaskInfoSqlByTaskId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(taskService.getSqlAdminByTaskId(id)));
    }

    @PostMapping("/info/sql/execute")
    public ResponseEntity<ResultDto<?>> executeSql(@RequestBody ExecuteSqlDto executeCodeDto, @AuthenticationPrincipal(expression = "id") Long id) {
        taskService.executeSql(executeCodeDto, id);
        return ResponseEntity.ok(new ResultDto<>());
    }

    @DeleteMapping ("/info/sql/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<?>> deleteSql(@PathVariable Long id) {
        taskService.deleteTaskInfoSql(id);
        return ResponseEntity.ok(new ResultDto<>());
    }
}
