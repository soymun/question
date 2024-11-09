package com.example.site.repository;

import com.example.site.model.Courses;
import com.example.site.model.util.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Courses, Long> {

    @Query(
            value = """
                    from Courses c
                    where (:tQ = true and c.userCreated.id = :teacher) or
                    ((:tQ is null or :tQ = false) and ((c.deleted = false)
                    and ( c.userCreated.id = :userId or true=:admin or ((c.open = true or exists(select 1 from UserCourse uc where uc.userCourseId.user.id = :userId and uc.userCourseId.courses.id = c.id and uc.deleted = false)) and (c.timeExecute IS NULL or current_timestamp <= c.timeExecute)))
                    and (c.courseName ilike :query or c.about ilike :query or :query is NULL)))
                    """
    )
    List<Courses> getAll(@Param("teacher") Long teacherId,
                         @Param("query") String query,
                         @Param("userId") Long userId,
                         @Param("admin") Boolean admin,
                         @Param("tQ") Boolean q);


    @Query(value = "from Courses c where c.id = :id and (c.userCreated.id=:tId or c.open or true  = :admin)")
    Optional<Courses> findByIdAndUserAndAdminAndUser(@Param("id") Long courseId, @Param("tId") Long tId, @Param("admin") boolean admin);

    @Query(value = "select t.courses from Task t where t.id=:id")
    Optional<Courses> getCoursesByTaskId(@Param("id") Long taskId);

    @Query("select (c.timeExecute > current_timestamp) from Task t left join Courses  c on t.courses.id = c.id where t.id=:id")
    Boolean getPredicateCourse(@Param("id") Long taskId);

    @Query(value = "select count(*) from Task t where t.courses.id = :id and t.taskType in (:types)")
    Long getCourseSqlById(@Param("id") Long courseId, @Param("types") List<TaskType> types);
}
