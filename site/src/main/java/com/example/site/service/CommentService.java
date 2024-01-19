package com.example.site.service;

import com.example.site.dto.comments.CommentCreateDto;
import com.example.site.dto.comments.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto saveComment(CommentCreateDto commentCreateDto);

    void deleteComment(Long id);

    void applyComment(Long id);

    List<CommentDto> getCommentByTaskId(Long id);

    List<CommentDto> getNotApplyCommentByTaskId(Long id);
}
