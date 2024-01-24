package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.course.CourseDto;
import com.example.site.dto.user_course.UserCourseCreateDto;
import com.example.site.dto.user_course.UserCourseDto;
import com.example.site.dto.user_course.UserCourseUpdate;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.impl.UserCourseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/courses")
public class UserCourseController {

    private final UserCourseServiceImpl userCourseService;


    @GetMapping("/course/{id}")
    public ResponseEntity<ResultDto<UserCourseDto>> getUserCourseByUserIdAndCourseId(@PathVariable Long id, @AuthenticationPrincipal UserDetailImpl principal) {
        return ResponseEntity.ok(new ResultDto<>(userCourseService.getUserCourseByUserIdAndCourseId(id, principal.getId())));
    }

    @GetMapping("/course/{id}/group/{gId}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<UserCourseDto>>> getUserByCourseIdAndGroupId(@PathVariable Long id, @PathVariable Long gId, @AuthenticationPrincipal UserDetailImpl principal) {
        return ResponseEntity.ok(new ResultDto<>(userCourseService.getUserByCourseIdAndGroupId(id, gId, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<UserCourseDto>> saveCourse(@RequestBody UserCourseCreateDto courseCreateDto, @AuthenticationPrincipal UserDetailImpl principal) {
        userCourseService.saveUserCourse(courseCreateDto.getUserId(), courseCreateDto.getCourseId(), principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN")));
        return ResponseEntity.status(201).build();
    }

    @PutMapping
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> updateCourse(@RequestBody UserCourseUpdate courseCreateDto) {
        userCourseService.updateUserCourse(courseCreateDto);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/user/{id}/course/{cid}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> deleteCourse(@PathVariable Long cid, @PathVariable Long id, @AuthenticationPrincipal UserDetailImpl principal) {
        userCourseService.deleteCourse(id,cid, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN")));
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/course/{id}/group/{cid}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> deleteCourseByGroup(@PathVariable Long cid, @PathVariable Long id, @AuthenticationPrincipal UserDetailImpl principal) {
        userCourseService.deleteUserCoursesByGroupId(id,cid, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN")));
        return ResponseEntity.status(204).build();
    }
}
