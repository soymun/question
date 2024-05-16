package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.course.CourseDto;
import com.example.site.dto.usercourse.UserCourseCreateDto;
import com.example.site.dto.usercourse.UserCourseDto;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.impl.UserCourseServiceImpl;
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
@RequiredArgsConstructor
@Tag(name = "UserCourse Controller")
@RequestMapping("/user/courses")
public class UserCourseController {

    private final UserCourseServiceImpl userCourseService;


    @GetMapping("/course/{id}")
    @Operation(description = "Получение курса для пользователя")
    public ResponseEntity<ResultDto<UserCourseDto>> getUserCourseByUserIdAndCourseId(@PathVariable Long id, @AuthenticationPrincipal(expression = "id") Long idUser) {
        return ResponseEntity.ok(new ResultDto<>(userCourseService.getUserCourseByUserIdAndCourseId(id, idUser)));
    }

    @GetMapping("/course/{id}/group/{gId}")
    @PreAuthorize("hasAuthority('TEACHER')")
    @Operation(description = "Получение результатов пользователей по id курсу и группе")
    public ResponseEntity<ResultDto<List<UserCourseDto>>> getUserByCourseIdAndGroupId(@PathVariable Long id, @PathVariable Long gId, @AuthenticationPrincipal(expression = "id") Long idUser, @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        return ResponseEntity.ok(new ResultDto<>(userCourseService.getUserByCourseIdAndGroupId(id, gId, idUser, grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN")))));
    }

    @PostMapping
    @Operation(description = "Добавление пользователя курса")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<UserCourseDto>> saveCourse(@Valid @RequestBody UserCourseCreateDto courseCreateDto, @AuthenticationPrincipal(expression = "id") Long idUser, @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        userCourseService.saveUserCourse(courseCreateDto.getUserId(), courseCreateDto.getCourseId(), idUser, grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN")));
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/course/{cId}/group/{id}")
    @Operation(description = "Добавление группы в курс")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<UserCourseDto>> saveGroupCourse(@PathVariable Long id, @Valid @PathVariable Long cId, @AuthenticationPrincipal(expression = "id") Long idUser, @AuthenticationPrincipal(expression = "grantedAuthorities") List<? extends GrantedAuthority> grantedAuthorities) {
        userCourseService.saveUserCourseGroup(id, cId, idUser, grantedAuthorities.stream().anyMatch(p -> p.getAuthority().equals("ADMIN")));
        return ResponseEntity.status(201).build();
    }
    
    @DeleteMapping("/user/{id}/course/{cid}")
    @Operation(description = "Удаление пользователя из курса")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> deleteCourse(@PathVariable Long cid, @PathVariable Long id, @AuthenticationPrincipal UserDetailImpl principal) {
        userCourseService.deleteCourse(id,cid, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN")));
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/course/{id}/group/{cid}")
    @Operation(description = "Удаление группы пользователей из курса")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<CourseDto>> deleteCourseByGroup(@PathVariable Long cid, @PathVariable Long id, @AuthenticationPrincipal UserDetailImpl principal) {
        userCourseService.deleteUserCoursesByGroupId(id,cid, principal.getId(), principal.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN")));
        return ResponseEntity.status(204).build();
    }
}
