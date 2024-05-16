package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.course.CourseCreateDto;
import com.example.site.dto.course.CourseDto;
import com.example.site.dto.course.CourseUpdateDto;
import com.example.site.dto.course.ExecuteSqlDto;
import com.example.site.service.impl.CourseServiceImpl;
import dto.ResponseExecuteSql;
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

    @GetMapping
    @Operation(description = "Получение всех курсов")
    public ResponseEntity<ResultDto<List<CourseDto>>> getAll(@RequestParam int pageNumber, @RequestParam int pageSize, @AuthenticationPrincipal(expression = "id") Long id, @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        return ResponseEntity.ok(new ResultDto<>(courseService.getAll(pageNumber, pageSize, id, grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }

    @GetMapping("/query")
    @Operation(description = "Получение курсов по запросу")
    public ResponseEntity<ResultDto<List<CourseDto>>> getAllByQuery(@RequestParam String query, @RequestParam int pageNumber, @RequestParam int pageSize, @AuthenticationPrincipal(expression = "id") Long id, @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        return ResponseEntity.ok(new ResultDto<>(courseService.getByQuery(query, pageNumber, pageSize, id, grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }

    @GetMapping("/{id}")
    @Operation(description = "Получение курса по id")
    public ResponseEntity<ResultDto<CourseDto>> getById(@PathVariable Long id, @AuthenticationPrincipal(expression = "id") Long idUser, @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        return ResponseEntity.ok(new ResultDto<>(courseService.getById(id, idUser, grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }

    @PostMapping
    @Operation(description = "Создание нового курса")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> saveCourse(@Valid @RequestBody CourseCreateDto courseCreateDto) {
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

    @GetMapping("/teacher/{id}")
    @Operation(description = "Получение всех курсов для учителя")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<CourseDto>>> getAllByTeacher(@PathVariable Long id, @RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok(new ResultDto<>(courseService.getAllByTeacher(id, pageNumber, pageSize)));
    }

    @PostMapping("/execute")
    @Operation(description = "Выполнение sql для настройки(выполнять только после создания хотя бы 1 задачи по sql)")
    public ResponseEntity<ResultDto<List<ResponseExecuteSql>>> executeSql(@RequestBody ExecuteSqlDto requestExecuteSql, @AuthenticationPrincipal(expression = "id") Long id, @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        return ResponseEntity.ok(new ResultDto<>(courseService.executeSqlInCourse(requestExecuteSql, id, grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }
}
