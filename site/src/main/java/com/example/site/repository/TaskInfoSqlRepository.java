package com.example.site.repository;

import com.example.site.model.TaskInfoSql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskInfoSqlRepository extends JpaRepository<TaskInfoSql, Long> {

    @Query(value = "from TaskInfoSql ts where ts.task.id=:id")
    Optional<TaskInfoSql> getByTaskId(@Param("id") Long id);
}
