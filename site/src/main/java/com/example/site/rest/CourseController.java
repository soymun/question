package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.course.CourseCreateDto;
import com.example.site.dto.course.CourseDto;
import com.example.site.dto.course.CourseUpdateDto;
import com.example.site.dto.course.ExecuteSqlDto;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.impl.CourseServiceImpl;
import dto.ResponseExecuteSql;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl courseService;

    @GetMapping
    public ResponseEntity<ResultDto<List<CourseDto>>> getAll(@RequestParam int pageNumber, @RequestParam int pageSize, @AuthenticationPrincipal UserDetailImpl principal) {
        return ResponseEntity.ok(new ResultDto<>(courseService.getAll(pageNumber, pageSize, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }

    @GetMapping("/query")
    public ResponseEntity<ResultDto<List<CourseDto>>> getAllByQuery(@RequestParam String query, @RequestParam int pageNumber, @RequestParam int pageSize, @AuthenticationPrincipal UserDetailImpl principal) {
        return ResponseEntity.ok(new ResultDto<>(courseService.getByQuery(query, pageNumber, pageSize, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultDto<CourseDto>> getById(@PathVariable Long id, @AuthenticationPrincipal UserDetailImpl principal) {
        return ResponseEntity.ok(new ResultDto<>(courseService.getById(id, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> saveCourse(@RequestBody CourseCreateDto courseCreateDto) {
        return ResponseEntity.ok(new ResultDto<>(courseService.saveCourse(courseCreateDto)));
    }

    @PutMapping
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> updateCourse(@RequestBody CourseUpdateDto courseCreateDto, @AuthenticationPrincipal UserDetailImpl principal) {
        return ResponseEntity.ok(new ResultDto<>(courseService.updateCourse(courseCreateDto, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> deleteCourse(@PathVariable Long id, @AuthenticationPrincipal UserDetailImpl principal) {
        courseService.deleteCourse(id, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN")));
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/teacher/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<CourseDto>>> getAllByTeacher(@PathVariable Long id, @RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok(new ResultDto<>(courseService.getAllByTeacher(id, pageNumber, pageSize)));
    }

    @PostMapping("/execute")
    public ResponseEntity<ResultDto<List<ResponseExecuteSql>>> executeSql(@RequestBody ExecuteSqlDto requestExecuteSql, @AuthenticationPrincipal UserDetailImpl principal) {
        return ResponseEntity.ok(new ResultDto<>(courseService.executeSqlInCourse(requestExecuteSql, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }
}
