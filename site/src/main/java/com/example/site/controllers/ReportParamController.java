package com.example.site.controllers;

import com.example.site.dto.ResultDto;
import com.example.site.dto.dcreportparamtype.DcReportParamTypeDto;
import com.example.site.dto.reportparam.ReportParamCreateDto;
import com.example.site.dto.reportparam.ReportParamDto;
import com.example.site.service.ReportParamService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report/param")
@RequiredArgsConstructor
public class ReportParamController {

    private final ReportParamService reportParamService;

    @PostMapping
    @Operation(description = "Добавление параметра")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<ReportParamDto>> saveParam(@Valid @RequestBody ReportParamCreateDto dto) {
        return ResponseEntity.ok(new ResultDto<>(reportParamService.create(dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Удаление параметра")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<DcReportParamTypeDto>> deleteParam(@PathVariable Long id) {
        reportParamService.delete(id);
        return ResponseEntity.status(201).build();
    }

    @GetMapping()
    @Operation(description = "Получить все значения")
    public ResponseEntity<ResultDto<List<ReportParamDto>>> getAllParam() {
        return ResponseEntity.ok(new ResultDto<>(reportParamService.getAll()));
    }

    @GetMapping("/report/{id}")
    @Operation(description = "Получить все значения по отчёту")
    public ResponseEntity<ResultDto<List<ReportParamDto>>> getParamByReport(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(reportParamService.getAllByReportId(id)));
    }

    @GetMapping("/{id}")
    @Operation(description = "Получить параметр")
    public ResponseEntity<ResultDto<ReportParamDto>> getParam(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(reportParamService.getById(id)));
    }

    @PatchMapping
    @Operation(description = "Изменения в параметр")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<ReportParamDto>> update(@Valid @RequestBody ReportParamDto dto) {
        return ResponseEntity.ok(new ResultDto<>(reportParamService.update(dto)));
    }
}
