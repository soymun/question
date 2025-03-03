package com.example.site.repository;

import com.example.site.model.UserCourse;
import com.example.site.model.UserCourseId;
import com.example.site.model.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserCourseRepository extends JpaRepository<UserCourse, UserCourseId> {

    @Query(value = "from UserCourse uc where uc.userCourseId.courses.id = :cId and (select count(*) from UserTask ut where ut.userTaskId.user.id = uc.userCourseId.user.id and ut.userTaskId.task.courses.id = uc.userCourseId.courses.id and ut.rights) >= :count and uc.courseMarks.countTask < :count")
    List<UserCourse> getUserCourseByCountTask(@Param("count") Long countTask, @Param("cId") Long coursesId);

    @Query(value = "from UserCourse uc where uc.userCourseId.courses.id = :cId and uc.courseMarks.id = :markId")
    List<UserCourse> getUserCourseByCourseIdAndMark(@Param("cId") Long courseId, @Param("markId") Long markId);

    @Query(value = "from UserCourse uc where uc.userCourseId.courses.id = :cId")
    List<UserCourse> getUserCourseByCourseId(@Param("cId") Long courseId);

    @Query(value = "from UserCourse  uc where uc.userCourseId.courses.id = :cId and uc.userCourseId.user.id=:userId and (uc.userCourseId.courses.userCreated.id = :tId or :admin = true)")
    Optional<UserCourse> getUserCourseByUserIdAndCourseAndTeacherAndAdmin(@Param("userId")Long userId, @Param("cId") Long courseId, @Param("tId") Long teacherId, @Param("admin") boolean admin);


    @Query(value = "from UserCourse  uc where uc.userCourseId.courses.id = :cId and uc.userCourseId.user.groups.id=:groupId and (uc.userCourseId.courses.userCreated.id = :tId or :admin = true) and uc.deleted=false")
    List<UserCourse> getAllByCourseIdAndGroupId(@Param("groupId")Long groupId, @Param("cId") Long courseId, @Param("tId") Long teacherId, @Param("admin") boolean admin);

    @Query(value = "select uc as userCourse, ut as userTask from UserCourse uc left join UserTask ut on uc.userCourseId.courses.id = ut.userTaskId.task.courses.id and uc.userCourseId.user.id = ut.userTaskId.user.id where uc.userCourseId.courses.id = :cId and uc.userCourseId.user.groups.id=:groupId and (uc.userCourseId.courses.userCreated.id = :tId or :admin = true) and uc.deleted=false")
    List<UserCourseProjection> getAllByCourseIdAndGroupIdAndUserTask(@Param("groupId")Long groupId, @Param("cId") Long courseId, @Param("tId") Long teacherId, @Param("admin") boolean admin);

    @Query(value = "from UserCourse  uc where uc.userCourseId.courses.id = :cId and uc.userCourseId.user.id=:userId")
    Optional<UserCourse> getUserCourseByUserIdAndCourse(@Param("userId")Long userId, @Param("cId") Long courseId);


    @Query(value = "from UserCourse  uc where uc.userCourseId.user.id=:userId")
    List<UserCourse> getByUserId(@Param("userId")Long userId);


    interface UserCourseProjection {

        UserCourse getUserCourse();

        UserTask getUserTask();

    }
}
