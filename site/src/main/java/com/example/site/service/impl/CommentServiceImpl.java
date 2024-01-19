package com.example.site.service.impl;

import com.example.site.dto.comments.CommentCreateDto;
import com.example.site.dto.comments.CommentDto;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.CommentMapper;
import com.example.site.model.Comments;
import com.example.site.repository.CommentRepository;
import com.example.site.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Override
    public CommentDto saveComment(CommentCreateDto commentCreateDto) {

        Comments comments = commentMapper.commentCreateDtoToComment(commentCreateDto);

        comments.setApply(false);
        comments.setTime(LocalDateTime.now());

        return commentMapper.commentToCommentDto(commentRepository.save(comments));
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void applyComment(Long id) {
        Comments comments = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Комментарий не найден"));

        comments.setApply(true);

        commentRepository.save(comments);
    }

    @Override
    public List<CommentDto> getCommentByTaskId(Long id) {
        return commentRepository.getAllByTaskIdAndApply(id).stream().map(commentMapper::commentToCommentDto).toList();
    }

    @Override
    public List<CommentDto> getNotApplyCommentByTaskId(Long id) {
        return commentRepository.getAllByTaskIdAndNotApply(id).stream().map(commentMapper::commentToCommentDto).toList();
    }
}
