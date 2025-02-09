package com.example.site.dto.notification;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class NotificationDto {

    private Map<String, Object> values;

    private NotificationType type;

    private String email;
}
