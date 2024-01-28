package com.example.site.repository;

import com.example.site.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "from Task t where t.courses.id = :cId and t.deleted = FALSE ORDER BY t.number")
    List<Task> findAllByCourseId(@Param("cId") Long courseId);


    @Query(value = "from Task t where t.courses.id = :cId and t.deleted = FALSE and t.open = TRUE ORDER BY t.number")
    List<Task> findAllByCourseIdWithoutCreated(@Param("cId") Long courseId);
}
