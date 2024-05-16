package com.example.site.rest;


import com.example.site.dto.ResultDto;
import com.example.site.dto.group.GroupCreateDto;
import com.example.site.dto.group.GroupDto;
import com.example.site.service.impl.GroupServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Group Controller")
@RequestMapping("/groups")
public class GroupController {

    private final GroupServiceImpl groupService;

    @GetMapping
    @Operation(description = "Получение всех групп")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<GroupDto>>> getAll(){
        return ResponseEntity.ok(new ResultDto<>(groupService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(description = "Получение группы по id")
    public ResponseEntity<ResultDto<GroupDto>> getById(@PathVariable Long id){
        return ResponseEntity.ok(new ResultDto<>(groupService.getById(id)));
    }

    @GetMapping("/name/{name}")
    @Operation(description = "Поиск группы по имени")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<GroupDto>>> getAllByName(@PathVariable String name){
        return ResponseEntity.ok(new ResultDto<>(groupService.getByName(name)));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Удаление группы")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<GroupDto>> deleteById(@PathVariable Long id){
        groupService.deleteGroup(id);
        return ResponseEntity.status(203).build();
    }

    @PostMapping
    @Operation(description = "Создание группы")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<GroupDto>> saveGroup(@Valid  @RequestBody GroupCreateDto groupCreateDto){
        return ResponseEntity.ok(new ResultDto<>(groupService.saveGroup(groupCreateDto)));
    }

    @PatchMapping
    @Operation(description = "Изменение группы")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<GroupDto>> updateGroup(@Valid @RequestBody GroupDto groupCreateDto){
        return ResponseEntity.ok(new ResultDto<>(groupService.updateGroup(groupCreateDto)));
    }

    @GetMapping("/course/{id}")
    @Operation(description = "Получение всех групп по курсу")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<GroupDto>>> getAllByCourseId(@PathVariable Long id){
        return ResponseEntity.ok(new ResultDto<>(groupService.getGroupByCourseId(id)));
    }
}
