package com.example.site.repository;

import com.example.site.model.Courses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Courses, Long> {

    @Query(
            value = "from Courses c where c.courseName like concat(:query, '%') and c.deleted = FALSE and (((c.open or exists(select 1 from UserCourse uc where uc.userCourseId.user.id = :userId and uc.userCourseId.courses.id =:cId and uc.deleted = FALSE)) and (c.timeExecute IS NULL or current timestamp  <= c.timeExecute)) or c.userCreated.id = :userId or true=:admin)",
            countQuery = "SELECT count(c) from Courses c where c.courseName like concat(:query, '%') and c.deleted = FALSE and (((c.open or exists(select 1 from UserCourse uc where uc.userCourseId.user.id = :userId and uc.userCourseId.courses.id =:cId and uc.deleted = FALSE)) and (c.timeExecute IS NULL or current timestamp  <= c.timeExecute)) or c.userCreated.id = :userId or true=:admin)"
    )
    Page<Courses> getCoursesByQuery(@Param("query") String query, @Param("userId") Long userId, @Param("admin") Boolean admin, Pageable pageable);


    @Query(
            value = "from Courses c where c.deleted = false and ( c.userCreated.id = :userId or true=:admin or ((c.open = true or exists(select 1 from UserCourse uc where uc.userCourseId.user.id = :userId and uc.userCourseId.courses.id = c.id and uc.deleted = false)) and (c.timeExecute IS NULL or current timestamp <= c.timeExecute)))",
            countQuery = "SELECT count(c) from Courses c where c.deleted = false and ( c.userCreated.id = :userId or true=:admin or ((c.open  = true or exists(select 1 from UserCourse uc where uc.userCourseId.user.id = :userId and uc.userCourseId.courses.id = c.id and uc.deleted = false)) and (c.timeExecute IS NULL or current timestamp <= c.timeExecute)))"
    )
    Page<Courses> getAll(@Param("userId") Long userId, @Param("admin") Boolean admin, Pageable pageable);


    @Query(
            value = "from Courses c where c.userCreated.id = :tId",
            countQuery = "SELECT count(c) from Courses c where c.userCreated.id = :tId"
    )
    Page<Courses> getAllByTeacherId(@Param("tId") Long tId, Pageable pageable);

    @Query(value = "from Courses c where c.id = :id and (c.userCreated.id=:tId or c.open or true  = :admin)")
    Optional<Courses> findByIdAndUserAndAdminAndUser(@Param("id") Long courseId, @Param("tId") Long tId, @Param("admin") boolean admin);

    @Query(value = "select t.courses from Task t where t.id=:id")
    Optional<Courses> getCoursesByTaskId(@Param("id") Long taskId);

    @Query("select (c.timeExecute > current timestamp) from Task t left join Courses  c on t.courses.id = c.id where t.id=:id")
    Boolean getPredicateCourse(@Param("id") Long taskId);
}
