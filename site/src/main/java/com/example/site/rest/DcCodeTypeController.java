package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.dccodetype.DcCodeTypeCreateDto;
import com.example.site.dto.dccodetype.DcCodeTypeDto;
import com.example.site.service.DcCodeTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dc/code-type")
public class DcCodeTypeController {

    private final DcCodeTypeService dcCodeTypeService;

    @PostMapping
    @Operation(description = "Добавление в словарь")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<DcCodeTypeDto>> saveDc(@Valid @RequestBody DcCodeTypeCreateDto dto) {
        return ResponseEntity.ok(new ResultDto<>(dcCodeTypeService.create(dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Удаление словарь")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<DcCodeTypeDto>> deleteDc(@PathVariable Long id) {
        dcCodeTypeService.deleteById(id);
        return ResponseEntity.status(201).build();
    }

    @GetMapping()
    @Operation(description = "Получить все значения")
    public ResponseEntity<ResultDto<List<DcCodeTypeDto>>> getAllDc() {
        return ResponseEntity.ok(new ResultDto<>(dcCodeTypeService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(description = "Получить все значения")
    public ResponseEntity<ResultDto<DcCodeTypeDto>> getDcById(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(dcCodeTypeService.getById(id)));
    }

    @PatchMapping
    @Operation(description = "Изменения в словарь")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<DcCodeTypeDto>> update(@Valid @RequestBody DcCodeTypeDto dto) {
        return ResponseEntity.ok(new ResultDto<>(dcCodeTypeService.update(dto)));
    }
}
