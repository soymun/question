package com.example.site.rest;

import com.example.site.dto.ResultDto;
import com.example.site.dto.comments.CommentCreateDto;
import com.example.site.dto.comments.CommentDto;
import com.example.site.dto.marks.MarkDto;
import com.example.site.service.impl.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping
    public ResponseEntity<ResultDto<CommentDto>> saveComment(@RequestBody CommentCreateDto commentCreateDto){
        return ResponseEntity.ok(new ResultDto<>(commentService.saveComment(commentCreateDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<MarkDto>> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/apply/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<MarkDto>> applyComment(@PathVariable Long id){
        commentService.applyComment(id);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/task/{id}/all")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<CommentDto>>> getAllByTaskAll(@PathVariable Long id){
        return ResponseEntity.ok(new ResultDto<>(commentService.getNotApplyCommentByTaskId(id)));
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<ResultDto<List<CommentDto>>> getAllByTask(@PathVariable Long id){
        return ResponseEntity.ok(new ResultDto<>(commentService.getCommentByTaskId(id)));
    }
}
