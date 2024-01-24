package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.history.HistoryDto;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.impl.TaskHistoryServiceImpl;
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
@RequiredArgsConstructor
@RequestMapping("/task/history")
public class TaskHistoryController {

    private final TaskHistoryServiceImpl taskHistoryService;

    @GetMapping("/task/{id}")
    public ResponseEntity<ResultDto<List<HistoryDto>>> getAllByTaskAll(@PathVariable Long id){
        return ResponseEntity.ok(new ResultDto<>(taskHistoryService.getAllByTask(id)));
    }

    @GetMapping("/task/user/{id}")
    public ResponseEntity<ResultDto<List<HistoryDto>>> getAllByTaskAndUserAll(@PathVariable Long id, @AuthenticationPrincipal UserDetailImpl principal){
        return ResponseEntity.ok(new ResultDto<>(taskHistoryService.getAllByTaskAndUserId(id, principal.getId())));
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<HistoryDto>>> getAllByUserAll(@AuthenticationPrincipal UserDetailImpl principal){
        return ResponseEntity.ok(new ResultDto<>(taskHistoryService.getAllByUserId(principal.getId())));
    }
}
