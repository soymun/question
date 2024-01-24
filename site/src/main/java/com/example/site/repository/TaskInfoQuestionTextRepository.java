package com.example.site.repository;

import com.example.site.model.TaskInfoQuestionText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskInfoQuestionTextRepository extends JpaRepository<TaskInfoQuestionText, Long> {

    @Query(value = "from TaskInfoQuestionText tt where tt.task.id=:id")
    Optional<TaskInfoQuestionText> findByTaskId(@Param("id") Long id);
}
