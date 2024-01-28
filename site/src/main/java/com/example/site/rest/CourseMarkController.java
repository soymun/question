package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.marks.MarkCreateDto;
import com.example.site.dto.marks.MarkDto;
import com.example.site.service.impl.CourseMarkServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course/marks")
public class CourseMarkController {

    private final CourseMarkServiceImpl courseMarkService;

    @PostMapping
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<MarkDto>> saveMark(@RequestBody MarkCreateDto markCreateDto){
        return ResponseEntity.ok(new ResultDto<>(courseMarkService.saveMark(markCreateDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<MarkDto>> deleteMark(@PathVariable Long id){
        courseMarkService.deleteMark(id);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/course/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<MarkDto>>> getAllByCourse(@PathVariable Long id){
        return ResponseEntity.ok(new ResultDto<>(courseMarkService.getAllByCourse(id)));
    }
}
