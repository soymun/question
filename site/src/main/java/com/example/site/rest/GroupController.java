package com.example.site.rest;


import com.example.site.dto.ResultDto;
import com.example.site.dto.group.GroupCreateDto;
import com.example.site.dto.group.GroupDto;
import com.example.site.service.impl.GroupServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    private final GroupServiceImpl groupService;

    @GetMapping()
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<GroupDto>>> getAll(){
        return ResponseEntity.ok(new ResultDto<>(groupService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultDto<GroupDto>> getById(@PathVariable Long id){
        return ResponseEntity.ok(new ResultDto<>(groupService.getById(id)));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<GroupDto>>> getAllByName(@PathVariable String name){
        return ResponseEntity.ok(new ResultDto<>(groupService.getByName(name)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<GroupDto>> deleteById(@PathVariable Long id){
        groupService.deleteGroup(id);
        return ResponseEntity.status(201).build();
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<GroupDto>> saveGroup(@RequestBody GroupCreateDto groupCreateDto){
        return ResponseEntity.ok(new ResultDto<>(groupService.saveGroup(groupCreateDto)));
    }

    @PatchMapping
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ResponseEntity<ResultDto<GroupDto>> updateGroup(@RequestBody GroupDto groupCreateDto){
        return ResponseEntity.ok(new ResultDto<>(groupService.updateGroup(groupCreateDto)));
    }

    @GetMapping("/course/{id}")
    @PreAuthorize(value = "hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<GroupDto>>> getAllByCourseId(@PathVariable Long id){
        return ResponseEntity.ok(new ResultDto<>(groupService.getGroupByCourseId(id)));
    }
}
