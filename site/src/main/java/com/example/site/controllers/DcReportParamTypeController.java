package com.example.site.controllers;

import com.example.site.dto.ResultDto;
import com.example.site.dto.dcreportparamtype.DcReportParamTypeCreateDto;
import com.example.site.dto.dcreportparamtype.DcReportParamTypeDto;
import com.example.site.service.DcReportParamTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dc/report-param-type")
public class DcReportParamTypeController {

    private final DcReportParamTypeService dcReportParamTypeService;

    @PostMapping
    @Operation(description = "Добавление в словарь")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<DcReportParamTypeDto>> saveDc(@Valid @RequestBody DcReportParamTypeCreateDto dto) {
        return ResponseEntity.ok(new ResultDto<>(dcReportParamTypeService.create(dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Удаление словарь")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<DcReportParamTypeDto>> deleteDc(@PathVariable Long id) {
        dcReportParamTypeService.deleteById(id);
        return ResponseEntity.status(201).build();
    }

    @GetMapping()
    @Operation(description = "Получить все значения")
    public ResponseEntity<ResultDto<List<DcReportParamTypeDto>>> getAllDc() {
        return ResponseEntity.ok(new ResultDto<>(dcReportParamTypeService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(description = "Получить все значения")
    public ResponseEntity<ResultDto<DcReportParamTypeDto>> getDcById(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(dcReportParamTypeService.getById(id)));
    }

    @PatchMapping
    @Operation(description = "Изменения в словарь")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<DcReportParamTypeDto>> update(@Valid @RequestBody DcReportParamTypeDto dto) {
        return ResponseEntity.ok(new ResultDto<>(dcReportParamTypeService.update(dto)));
    }
}
