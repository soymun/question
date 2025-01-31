package com.example.notification.repository;

import com.example.notification.model.NotificationTemplate;
import com.example.notification.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationTemplate, Integer> {

    @Query(value = "from NotificationTemplate nt where nt.notificationType = :type")
    List<NotificationTemplate> getAllByType(NotificationType type);
}
