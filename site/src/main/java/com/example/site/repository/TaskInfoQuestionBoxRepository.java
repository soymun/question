package com.example.site.repository;

import com.example.site.model.TaskInfoQuestionBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskInfoQuestionBoxRepository extends JpaRepository<TaskInfoQuestionBox, Long> {


    @Query(value = "select tb.id from TaskInfoQuestionBox tb where tb.task.id=:taskId and tb.rights = TRUE ")
    List<Long> getRightAnswerByTaskId(@Param("taskId") Long taskId);

    @Query(value = "from TaskInfoQuestionBox tb where tb.task.id=:taskId")
    List<TaskInfoQuestionBox> getByTaskId(@Param("taskId") Long taskId);

}
