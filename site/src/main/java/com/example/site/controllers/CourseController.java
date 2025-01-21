package com.example.site.controllers;

import com.example.site.dto.ResponseExecuteSql;
import com.example.site.dto.ResultDto;
import com.example.site.dto.course.*;
import com.example.site.service.impl.CourseServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@Tag(
        name = "Course controller"
)
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl courseService;

    @PostMapping("/search")
    @Schema(description = "Получение всех курсов")
    public ResponseEntity<ResultDto<List<CourseDto>>> getAll(@RequestBody CourseRequestDto courseRequestDto) {
        return ResponseEntity.ok(new ResultDto<>(courseService.getAll(courseRequestDto)));
    }

    @GetMapping("/{id}")
    @Schema(description = "Получение курса по id")
    public ResponseEntity<ResultDto<CourseDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(courseService.getById(id)));
    }

    @PostMapping
    @Schema(description = "Создание нового курса")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> saveCourse(@Valid @RequestBody CourseCreateDto courseCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(courseService.saveCourse(courseCreateDto)));
    }

    @PutMapping
    @Schema(description = "Изменение курса")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> updateCourse(@Valid @RequestBody CourseUpdateDto courseCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(courseService.updateCourse(courseCreateDto)));
    }

    @DeleteMapping("/{id}")
    @Schema(description = "Удаление курса")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/task/execute")
    @Schema(description = "Выполнение sql для настройки(выполнять только после создания хотя бы 1 задачи по sql)")
    public ResponseEntity<ResultDto<List<ResponseExecuteSql>>> executeSql(@RequestBody ExecuteSqlDto requestExecuteSql) {
        return ResponseEntity.ok(new ResultDto<>(courseService.executeSqlInCourse(requestExecuteSql)));
    }
}
