package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.history.HistoryDto;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.impl.TaskHistoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "TaskHistory Controller")
@RequiredArgsConstructor
@RequestMapping("/task/history")
public class TaskHistoryController {

    private final TaskHistoryServiceImpl taskHistoryService;

    @GetMapping("/task/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    @Operation(description = "Получение всех по задаче")
    public ResponseEntity<ResultDto<List<HistoryDto>>> getAllByTaskAll(@PathVariable Long id){
        return ResponseEntity.ok(new ResultDto<>(taskHistoryService.getAllByTask(id)));
    }

    @GetMapping("/task/user/{id}")
    @Operation(description = "Получение всех по задаче и пользователю")
    public ResponseEntity<ResultDto<List<HistoryDto>>> getAllByTaskAndUserAll(@PathVariable Long id, @AuthenticationPrincipal UserDetailImpl principal){
        return ResponseEntity.ok(new ResultDto<>(taskHistoryService.getAllByTaskAndUserId(id, principal.getId())));
    }

    @GetMapping("/user/{id}")
    @Operation(description = "Получение по пользователю")
    public ResponseEntity<ResultDto<List<HistoryDto>>> getAllByUserAll(@AuthenticationPrincipal UserDetailImpl principal){
        return ResponseEntity.ok(new ResultDto<>(taskHistoryService.getAllByUserId(principal.getId())));
    }
}
