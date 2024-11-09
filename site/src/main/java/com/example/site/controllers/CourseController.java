package com.example.site.controllers;

import com.example.site.dto.ResponseExecuteSql;
import com.example.site.dto.ResultDto;
import com.example.site.dto.course.*;
import com.example.site.service.impl.CourseServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @Operation(description = "Получение всех курсов")
    public ResponseEntity<ResultDto<List<CourseDto>>> getAll(@RequestBody CourseRequestDto courseRequestDto,
                                                             @AuthenticationPrincipal(expression = "id") Long id,
                                                             @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        return ResponseEntity.ok(new ResultDto<>(courseService.getAll(courseRequestDto, id, grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }

    @GetMapping("/{id}")
    @Operation(description = "Получение курса по id")
    public ResponseEntity<ResultDto<CourseDto>> getById(@PathVariable Long id, @AuthenticationPrincipal(expression = "id") Long idUser, @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        return ResponseEntity.ok(new ResultDto<>(courseService.getById(id, idUser, grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }

    @PostMapping
    @Operation(description = "Создание нового курса")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> saveCourse(@Valid @RequestBody CourseCreateDto courseCreateDto, @AuthenticationPrincipal(expression = "id") Long id) {
        courseCreateDto.setUserCreated(id);
        return ResponseEntity.ok(new ResultDto<>(courseService.saveCourse(courseCreateDto)));
    }

    @PutMapping
    @Operation(description = "Изменение курса")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> updateCourse(@Valid @RequestBody CourseUpdateDto courseCreateDto, @AuthenticationPrincipal(expression = "id") Long id, @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        return ResponseEntity.ok(new ResultDto<>(courseService.updateCourse(courseCreateDto, id, grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Удаление курса")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> deleteCourse(@PathVariable Long id, @AuthenticationPrincipal(expression = "id") Long idUser, @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        courseService.deleteCourse(id, idUser, grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN")));
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/execute")
    @Operation(description = "Выполнение sql для настройки(выполнять только после создания хотя бы 1 задачи по sql)")
    public ResponseEntity<ResultDto<List<ResponseExecuteSql>>> executeSql(@RequestBody ExecuteSqlDto requestExecuteSql, @AuthenticationPrincipal(expression = "id") Long id, @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        return ResponseEntity.ok(new ResultDto<>(courseService.executeSqlInCourse(requestExecuteSql, id, grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }
}
