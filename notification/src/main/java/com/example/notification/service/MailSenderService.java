package com.example.notification.service;

import com.example.notification.dto.NotificationDto;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender mailSender;
    private final NotificationTemplateService service;

    public void sendMail(NotificationDto notificationDto) {
        try {
            log.info("Sending mail to {}", notificationDto.getEmail());
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            service.getMessage(notificationDto, mimeMessageHelper);
            mimeMessageHelper.setTo(notificationDto.getEmail());
            mailSender.send(mimeMessage);
            log.info("Mail sent to {}", notificationDto.getEmail());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
