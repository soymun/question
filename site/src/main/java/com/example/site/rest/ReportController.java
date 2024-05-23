package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.report.ReportCreateDto;
import com.example.site.dto.report.ReportDto;
import com.example.site.dto.report.ReportInitDto;
import com.example.site.dto.report.ReportUpdateDto;
import com.example.site.model.util.Role;
import com.example.site.service.ReportService;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    @Operation(description = "Добавление отчёта")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<ReportDto>> saveReport(@Valid @RequestBody ReportCreateDto dto) {
        return ResponseEntity.ok(new ResultDto<>(reportService.saveReport(dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Удаление отчёта")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<ReportDto>> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.status(201).build();
    }

    @GetMapping()
    @Operation(description = "Получить все отчёты")
    public ResponseEntity<ResultDto<List<ReportDto>>> getAllReport(@AuthenticationPrincipal(expression = "role") Role role) {
        return ResponseEntity.ok(new ResultDto<>(reportService.getAllReport(role)));
    }

    @GetMapping("/{id}")
    @Operation(description = "Получить отчёт")
    public ResponseEntity<ResultDto<ReportDto>> getReportById(@PathVariable Long id, @AuthenticationPrincipal(expression = "role") Role role) {
        return ResponseEntity.ok(new ResultDto<>(reportService.getById(id, role)));
    }

    @PatchMapping
    @Operation(description = "Изменения в отчёты")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<ReportDto>> update(@Valid @RequestBody ReportUpdateDto dto) {
        return ResponseEntity.ok(new ResultDto<>(reportService.updateReport(dto)));
    }


    @PostMapping("/make")
    @Operation(description = "Выполнить отчёт")
    public ResponseEntity<Resource> makeReport(@Valid @RequestBody ReportInitDto dto) throws ServerException, JRException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, ClassNotFoundException, InternalException {
        return ResponseEntity.ok(reportService.makeReports(dto));
    }
}
