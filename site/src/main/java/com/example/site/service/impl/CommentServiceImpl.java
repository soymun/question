package com.example.site.service.impl;

import com.example.site.dto.comments.CommentCreateDto;
import com.example.site.dto.comments.CommentDto;
import com.example.site.exception.NotFoundException;
import com.example.site.mappers.CommentMapper;
import com.example.site.model.Comments;
import com.example.site.model.Task;
import com.example.site.repository.CommentRepository;
import com.example.site.repository.TaskRepository;
import com.example.site.repository.UserRepository;
import com.example.site.security.UserDetailImpl;
import com.example.site.service.CommentService;
import com.example.site.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Override
    public CommentDto saveComment(CommentCreateDto commentCreateDto) {
        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        log.info("Save comment user {}", userDetail.getId());

        Comments comments = commentMapper.commentCreateDtoToComment(commentCreateDto);
        comments.setUser(userRepository.getReferenceById(userDetail.getId()));

        Task task = taskRepository.getReferenceById(commentCreateDto.getTask());
        comments.setApply(Objects.equals(task.getCourses().getUserCreated().getId(), comments.getUser().getId()) || userDetail.isAdmin());
        comments.setCreateTime(LocalDateTime.now());

        return commentMapper.commentToCommentDto(commentRepository.save(comments));
    }

    @Override
    public void deleteComment(Long id) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        log.info("Delete comment id - {}, user - {}", id, userDetail.getId());
        commentRepository.deleteById(id);
    }

    @Override
    public void applyComment(Long id) {

        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        log.info("Apply comment id - {}, user - {}", id, userDetail.getId());

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
