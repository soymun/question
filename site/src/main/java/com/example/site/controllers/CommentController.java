package com.example.site.controllers;

import com.example.site.dto.ResultDto;
import com.example.site.dto.comments.CommentCreateDto;
import com.example.site.dto.comments.CommentDto;
import com.example.site.dto.marks.MarkDto;
import com.example.site.service.impl.CommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "Comment controller"
)
@RequestMapping("/comments")
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping
    @Operation(description = "Создание нового комментария")
    public ResponseEntity<ResultDto<CommentDto>> saveComment(@Valid @RequestBody CommentCreateDto commentCreateDto, @AuthenticationPrincipal(expression = "id") Long id){
        commentCreateDto.setUser(id);
        return ResponseEntity.ok(new ResultDto<>(commentService.saveComment(commentCreateDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Удаление комментария")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<MarkDto>> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/apply/{id}")
    @Operation(description = "Подтверждение комментария")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<MarkDto>> applyComment(@PathVariable Long id){
        commentService.applyComment(id);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/task/{id}/all")
    @Operation(description = "Поиск всех комментариев по задаче для учителя")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<ResultDto<List<CommentDto>>> getAllByTaskAll(@PathVariable Long id){
        return ResponseEntity.ok(new ResultDto<>(commentService.getNotApplyCommentByTaskId(id)));
    }

    @GetMapping("/task/{id}")
    @Operation(description = "Поиск всех комментариев по задаче")
    public ResponseEntity<ResultDto<List<CommentDto>>> getAllByTask(@PathVariable Long id){
        return ResponseEntity.ok(new ResultDto<>(commentService.getCommentByTaskId(id)));
    }
}
