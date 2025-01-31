package com.example.site.controllers;

import com.example.site.dto.ResponseExecuteSql;
import com.example.site.dto.ResultDto;
import com.example.site.dto.sandbox.SandBoxOpenDto;
import com.example.site.dto.sandbox.SandboxDto;
import com.example.site.dto.sandbox.SandboxExecuteDto;
import com.example.site.service.impl.SandboxService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.simpleframework.xml.core.Validate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sandbox")
public class SandBoxController {

    private final SandboxService sandboxService;

    @PostMapping("/execute")
    @Schema(description = "Выполнение задач в sandbox")
    public ResponseEntity<ResultDto<List<ResponseExecuteSql>>> process(@Schema(description = "Задача") @Validate @RequestBody SandboxExecuteDto sandboxExecuteDto) {
        return ResponseEntity.ok(new ResultDto<>(sandboxService.processInSandbox(sandboxExecuteDto)));
    }

    @GetMapping
    @Schema(description = "Поиск песочницы")
    public ResponseEntity<ResultDto<SandboxDto>> getSandbox() {
        return ResponseEntity.ok(new ResultDto<>(sandboxService.getSandbox()));
    }

    @GetMapping("/open")
    @Schema(description = "Поиск песочницы")
    public ResponseEntity<ResultDto<SandBoxOpenDto>> getSandboxOpen() {
        return ResponseEntity.ok(new ResultDto<>(sandboxService.getOpen()));
    }

    @PostMapping
    @Schema(description = "Сохранить песочницу")
    public ResponseEntity<ResultDto<?>> saveSandbox(@RequestBody @Valid SandboxDto sandboxDto) {
        sandboxService.saveSandbox(sandboxDto);
        return ResponseEntity.ok(new ResultDto<>());
    }
}
