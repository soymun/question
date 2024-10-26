package com.example.site.repository;

import com.example.site.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    @Query(value = "from User u where u.groups.id = :id",
            countQuery = "SELECT COUNT(u) from User u where u.groups.id = :id")
    Page<User> findUserByGroupId(@Param("id") Long id, Pageable pageable);

    @Query(value = "from User u where u.groups.id = :id")
    List<User> findUserByGroupId(@Param("id") Long id);


    @Query(value = "from User u where u.secondName ilike :name or u.firstName  ilike :name or  u.patronymic ilike :name")
    List<User> findUserByName(@Param("name") String name);
}
