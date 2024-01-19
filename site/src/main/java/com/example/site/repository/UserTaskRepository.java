package com.example.site.repository;

import com.example.site.model.UserTask;
import com.example.site.model.UserTaskId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserTaskRepository extends JpaRepository<UserTask, UserTaskId> {

    @Query(value = "from UserTask ut where ut.userTaskId.user.id=:uId and ut.userTaskId.task.courses.id=:cId and  ut.closed IS FALSE ORDER BY ut.userTaskId.task.number")
    List<UserTask> getUserTaskByUserIdAndCourseId(@Param("uId") Long userId, @Param("cId") Long courseId);

    @Query(value = "from UserTask ut where ut.userTaskId.user.groups.id=:gId and ut.userTaskId.task.courses.id=:cId")
    List<UserTask> getUserTaskByGroupIdAndCourseId(@Param("gId") Long groupId, @Param("cId") Long courseId);

    @Query(value = "from UserTask ut where ut.closed IS FALSE and ut.userTaskId.task.id=:taskId and ut.userTaskId.user.id=:userId")
    Optional<UserTask> getUserTaskByTaskIdAndUserId(@Param("userId") Long userId, @Param("taskId") Long taskId);
}
