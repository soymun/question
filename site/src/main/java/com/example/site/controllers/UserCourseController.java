package com.example.site.controllers;

import com.example.site.dto.ResultDto;
import com.example.site.dto.course.CourseDto;
import com.example.site.dto.usercourse.UserCourseCreateDto;
import com.example.site.dto.usercourse.UserCourseDto;
import com.example.site.service.impl.UserCourseServiceImpl;
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
@RequiredArgsConstructor
@Tag(name = "UserCourse Controller")
@RequestMapping("/user/courses")
public class UserCourseController {

    private final UserCourseServiceImpl userCourseService;

    @GetMapping("/course/{id}")
    @Schema(description = "Получение курса по пользователя")
    public ResponseEntity<ResultDto<UserCourseDto>> getUserCourseByUserIdAndCourseId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(userCourseService.getUserCourseByUserIdAndCourseId(id)));
    }

    @GetMapping("/user")
    @Schema(description = "Получение курса для пользователя")
    public ResponseEntity<ResultDto<List<UserCourseDto>>> getCoursesByUser() {
        return ResponseEntity.ok(new ResultDto<>(userCourseService.getCoursesToUser()));
    }

    @PostMapping("/course/add/{id}")
    @Schema(description = "Пользователь добавляется в курс")
    public ResponseEntity<ResultDto<UserCourseDto>> addUserCourseByUserIdAndCourseId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResultDto<>(userCourseService.addUserCourseByCourse(id)));
    }

    @PostMapping("/course/{сId}/add/{id}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @Schema(description = "Добавить пользователя в курс")
    public ResponseEntity<ResultDto<UserCourseDto>> addUser(@PathVariable Long id, @PathVariable Long сId) {
        return ResponseEntity.ok(new ResultDto<>(userCourseService.addUserCourseByUserIdAndCourseId(id, сId)));
    }

    @GetMapping("/course/{id}/group/{gId}")
    @PreAuthorize("hasAuthority('TEACHER')")
    @Schema(description = "Получение результатов пользователей по id курсу и группе")
    public ResponseEntity<ResultDto<List<UserCourseDto>>> getUserByCourseIdAndGroupId(@PathVariable Long id, @PathVariable Long gId) {
        return ResponseEntity.ok(new ResultDto<>(userCourseService.getUserByCourseIdAndGroupId(id, gId)));
    }

    @PostMapping
    @Schema(description = "Добавление пользователя курса")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<UserCourseDto>> saveCourse(@Valid @RequestBody UserCourseCreateDto courseCreateDto) {
        userCourseService.saveUserCourse(courseCreateDto.getUserId(), courseCreateDto.getCourseId());
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/course/{cId}/group/{id}")
    @Schema(description = "Добавление группы в курс")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<UserCourseDto>> saveGroupCourse(@PathVariable Long id, @Valid @PathVariable Long cId) {
        userCourseService.saveUserCourseGroup(id, cId);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/user/{id}/course/{cid}")
    @Schema(description = "Удаление пользователя из курса")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> deleteCourse(@PathVariable Long cid, @PathVariable Long id) {
        userCourseService.deleteCourse(id, cid);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/course/{id}/group/{cid}")
    @Schema(description = "Удаление группы пользователей из курса")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> deleteCourseByGroup(@PathVariable Long cid, @PathVariable Long id) {
        userCourseService.deleteUserCoursesByGroupId(id, cid);
        return ResponseEntity.status(204).build();
    }
}
