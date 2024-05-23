package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.marks.MarkCreateDto;
import com.example.site.dto.marks.MarkDto;
import com.example.site.service.impl.CourseMarkServiceImpl;
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
@Tag(name = "Mark controller")
@RequestMapping("/course/marks")
public class CourseMarkController {

    private final CourseMarkServiceImpl courseMarkService;

    @PostMapping
    @Operation(description = "Создание оценки")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<MarkDto>> saveMark(@Valid @RequestBody MarkCreateDto markCreateDto, @AuthenticationPrincipal(expression = "id") Long id){
        return ResponseEntity.ok(new ResultDto<>(courseMarkService.saveMark(markCreateDto, id)));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Удаление оценки")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<MarkDto>> deleteMark(@PathVariable Long id){
        courseMarkService.deleteMark(id);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/course/{id}")
    @Operation(description = "Получить все оценки по курсу")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<MarkDto>>> getAllByCourse(@PathVariable Long id){
        return ResponseEntity.ok(new ResultDto<>(courseMarkService.getAllByCourse(id)));
    }
}
