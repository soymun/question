package com.example.notification.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient(@Value("${minio.endpoint}") String endpoint,
                                   @Value("${minio.password}") String password,
                                   @Value("${minio.login}") String login) {
        return MinioClient
                .builder()
                .endpoint(endpoint)
                .credentials(login, password)
                .build();
    }
}
