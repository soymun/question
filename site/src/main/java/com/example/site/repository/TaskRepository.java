package com.example.site.repository;

import com.example.site.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "from Task t where t.courses.id = :cId and t.deleted = FALSE or t.deleted is null ORDER BY t.number")
    List<Task> findAllByCourseId(@Param("cId") Long courseId);

    @Query(value = "from Task t where t.courses.id = :cId  and t.taskGroup.id = :tgId and (t.deleted = FALSE or t.deleted is null) ORDER BY t.number")
    List<Task> findAllByCourseIdAndTaskGroup(@Param("cId") Long courseId,@Param("tgId") Long taskGroup);

    @Modifying
    @Query(value = "update Task t set t.allAttempt = t.allAttempt + 1 where t.id = :id")
    void updateAllAttempt(@Param("id") Long id);

    @Modifying
    @Query(value = "update Task t set t.rightAttempt = t.rightAttempt + 1 where t.id = :id")
    void updateRightAttempt(@Param("id") Long id);
}
