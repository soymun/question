package com.example.site.repository;

import com.example.site.model.CourseMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseMarksRepository extends JpaRepository<CourseMarks, Long> {

    @Query(value = "from CourseMarks cm where cm.courses.id = :cId and cm.countTask <= (select count(ut.userTaskId.task.id) FROM UserTask ut where ut.userTaskId.task.courses.id = :cId)")
    CourseMarks getCourseMarksLessCountByCourseId(@Param("cId") Long courseId);

    @Query(value = "from CourseMarks cm where cm.courses.id = :cId")
    List<CourseMarks> getCourseMarksByCourseId(@Param("cId") Long courseId);

    @Query(value = "from CourseMarks cm where cm.courses.id = :cId and cm.countTask = (select count(ut.userTaskId)from UserTask ut where ut.rights = TRUE and ut.userTaskId.user.id=:userId and ut.userTaskId.task.courses.id=:cId)")
    CourseMarks getCourseMarksByCourseIdAndCountTask(@Param("cId") Long courseId, @Param("userId")Long userId);
}
