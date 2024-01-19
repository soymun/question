package com.example.site.rest;

import com.example.site.dto.course.CourseCreateDto;
import com.example.site.dto.course.CourseUpdateDto;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.impl.CourseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl courseService;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam int pageNumber, @RequestParam int pageSize,@AuthenticationPrincipal UserDetailImpl principal){
        return ResponseEntity.ok(courseService.getAll(pageNumber, pageSize, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))));
    }

    @GetMapping("/query")
    public ResponseEntity<?> getAllByQuery(@RequestParam String query, @RequestParam int pageNumber,@RequestParam int pageSize,@AuthenticationPrincipal UserDetailImpl principal){
        return ResponseEntity.ok(courseService.getByQuery(query, pageNumber, pageSize, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id,@AuthenticationPrincipal UserDetailImpl principal){
        return ResponseEntity.ok(courseService.getById(id, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))));
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<?> saveCourse(@RequestBody CourseCreateDto courseCreateDto){
        return ResponseEntity.ok(courseService.saveCourse(courseCreateDto));
    }

    @PutMapping
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<?> updateCourse(@RequestBody CourseUpdateDto courseCreateDto,@AuthenticationPrincipal UserDetailImpl principal){
        return ResponseEntity.ok(courseService.updateCourse(courseCreateDto, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id,@AuthenticationPrincipal UserDetailImpl principal){
        courseService.deleteCourse(id, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN")));
        return ResponseEntity.status(204).build();
    }
}
