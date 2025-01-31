package com.example.notification.worker;

import com.example.notification.dto.NotificationDto;
import com.example.notification.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationWorker {

    private final MailSenderService mailSenderService;

    @RabbitListener(queues = {"notification"}, group = "notification")
    public void readNotification(NotificationDto notificationDto) {
        mailSenderService.sendMail(notificationDto);
    }
}
