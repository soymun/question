package com.example.site.repository;

import com.example.site.model.TaskInfoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskInfoCodeRepository extends JpaRepository<TaskInfoCode, Long> {

    @Query(value = "from TaskInfoCode tic where tic.task.id=:taskId")
    List<TaskInfoCode> getTaskInfoCodesByTaskId(@Param("taskId") Long taskId);

    @Query(value = "from TaskInfoCode tic where tic.task.id=:taskId and tic.codeType.id = :code")
    Optional<TaskInfoCode> getTaskInfoCodesByTaskIdAndCodeTypeId(@Param("taskId") Long taskId, @Param("code") Long code);
}
