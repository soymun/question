package com.example.site.repository;

import com.example.site.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {


    @Query(value = "from Comments  c where c.task.id=:tId and c.apply = true ")
    List<Comments> getAllByTaskIdAndApply(@Param("tId") Long taskId);

    @Query(value = "from Comments  c where c.task.id=:tId and c.apply = false ")
    List<Comments> getAllByTaskIdAndNotApply(@Param("tId") Long taskId);
}
