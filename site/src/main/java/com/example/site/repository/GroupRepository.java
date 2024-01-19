package com.example.site.repository;

import com.example.site.model.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Groups, Long> {

    @Query(value = "from Groups g where g.fullName like concat(:names, '%') or g.shortName like concat(:names, '%')")
    List<Groups> getAllByNames(@Param("names") String name);

    @Query(value = "SELECT DISTINCT uc.userCourseId.user.groups FROM UserCourse uc where uc.userCourseId.courses.id = :id and uc.deleted IS FALSE ")
    List<Groups> getAllGroupsByCourse(@Param("cId") Long id);
}
