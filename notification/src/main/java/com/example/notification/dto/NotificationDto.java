package com.example.notification.dto;

import com.example.notification.model.NotificationType;
import lombok.Data;

import java.util.Map;

@Data
public class NotificationDto {

    private Map<String, Object> values;

    private NotificationType type;

    private String email;

    private String subject;
}
