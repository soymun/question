package com.example.site.repository;

import com.example.site.model.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskGroupRepository extends JpaRepository<TaskGroup, Long> {


    @Query(value = "from TaskGroup tg where tg.courses.id = :id and tg.deleted != TRUE ORDER BY tg.number")
    List<TaskGroup> findByCourseId(@Param("id") Long id);
}
