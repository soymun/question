package com.example.site.repository;

import com.example.site.model.Sandbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SandboxRepository extends JpaRepository<Sandbox, Integer> {


    @Modifying
    @Query(value = "update Sandbox s set s.open = true")
    void openSandbox();


    @Modifying
    @Query(value = "update Sandbox s set s.open = false")
    void closeSandbox();
}
