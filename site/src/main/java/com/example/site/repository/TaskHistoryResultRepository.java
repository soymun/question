package com.example.site.repository;

import com.example.site.model.TaskHistoryResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskHistoryResultRepository extends JpaRepository<TaskHistoryResult, Long> {

    @Query(value = "from TaskHistoryResult tr where tr.task.id=:taskId")
    List<TaskHistoryResult> getAllByTaskId(@Param("taskId") Long id);

    @Query(value = "from TaskHistoryResult tr where tr.task.id=:taskId and tr.user.id=:userId")
    List<TaskHistoryResult> getAllByTaskIdAndUserId(@Param("taskId") Long id, @Param("userId") Long userId);

    @Query(value = "from TaskHistoryResult tr where tr.user.id=:userId")
    List<TaskHistoryResult> getAllByUserId(@Param("userId") Long id);
}
