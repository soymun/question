package com.example.site.repository;

import com.example.site.model.TaskHistoryResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskHistoryResultRepository extends JpaRepository<TaskHistoryResult, Long> {
}
