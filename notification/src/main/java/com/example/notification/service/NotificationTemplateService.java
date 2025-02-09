package com.example.notification.service;

import com.example.notification.dto.NotificationDto;
import com.example.notification.model.Buckets;
import com.example.notification.model.NotificationTemplate;
import com.example.notification.repository.NotificationRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationTemplateService {

    private final MinioClient minioClient;
    private final NotificationRepository notificationRepository;
    private final SpringTemplateEngine springTemplateEngine;
    @Value("${spring.mail.username}")
    private String from;

    @Transactional
    public void getMessage(NotificationDto notificationDto, MimeMessageHelper mimeMessageHelper) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, MessagingException {

        List<NotificationTemplate> notificationTemplates = notificationRepository.getAllByType(notificationDto.getType());

        if (notificationTemplates.size() != 1) {
            throw new RuntimeException("Notification template not found");
        }

        NotificationTemplate notificationTemplate = notificationTemplates.get(0);

        mimeMessageHelper.setSubject(notificationTemplate.getSubject());

        IContext context = new Context(new Locale("ru-RU"), notificationDto.getValues());

        String body = springTemplateEngine.process(new String(minioClient.getObject(GetObjectArgs.builder().bucket(Buckets.NOTIFICATION.getValue()).object(notificationTemplate.getFileName()).build()).readAllBytes()), context);

        mimeMessageHelper.setFrom(from + "@yandex.ru");
        mimeMessageHelper.setText(body, true);

    }
}
