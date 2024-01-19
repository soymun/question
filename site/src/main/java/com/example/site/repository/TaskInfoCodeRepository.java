package com.example.site.repository;

import com.example.site.model.TaskInfoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskInfoCodeRepository extends JpaRepository<TaskInfoCode, Long> {

    @Query(value = "from TaskInfoCode tic where tic.task.id=:taskId")
    List<TaskInfoCode> getTaskInfoCodesByTaskId(@Param("taskId") Long taskId);
}
