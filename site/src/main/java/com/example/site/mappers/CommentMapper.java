package com.example.site.mappers;

import com.example.site.dto.comments.CommentCreateDto;
import com.example.site.dto.comments.CommentDto;
import com.example.site.model.Comments;
import com.example.site.model.Task;
import com.example.site.model.User;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comments commentCreateDtoToComment(CommentCreateDto commentCreateDto);

    default User map1(Long id){
        return new User(id);
    }

    default Task map2(Long id){
        return new Task(id);
    }

    CommentDto commentToCommentDto(Comments comments);
}
