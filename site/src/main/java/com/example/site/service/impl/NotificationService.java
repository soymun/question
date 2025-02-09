package com.example.site.service.impl;

import com.example.site.dto.notification.NotificationDto;
import com.example.site.dto.notification.NotificationType;
import com.example.site.security.UserDetailImpl;
import com.example.site.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RabbitTemplate rabbitTemplate;


    public void sendNotification(String email, NotificationType notificationType, Map<String, Object> map) {

        NotificationDto notificationDto = NotificationDto
                .builder()
                .email(email)
                .type(notificationType)
                .values(map)
                .build();

        rabbitTemplate.convertAndSend("notification", notificationDto);

    }

    public void sendNotificationCurrentUser(NotificationType notificationType, Map<String, Object> map) {
        UserDetailImpl userDetail = SecurityUtil.getUserDetail();

        if (userDetail == null) return;

        sendNotification(userDetail.getEmail(), notificationType, map);
    }
}
