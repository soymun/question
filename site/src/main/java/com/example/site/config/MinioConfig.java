package com.example.site.config;

import com.example.site.util.BucketUtil;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinioConfig {

    @Value("${minio.login}")
    private String login;

    @Value("${minio.password}")
    private String password;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Bean
    public MinioClient minioClient() {

        MinioClient minioClient = MinioClient
                .builder()
                .endpoint(endpoint)
                .credentials(login, password)
                .build();

        initMinio(minioClient);

        return minioClient;
    }

    public void initMinio(MinioClient minioClient) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(BucketUtil.Buckets.REPORTS.value).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BucketUtil.Buckets.REPORTS.value).build());
            }

            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(BucketUtil.Buckets.FILES.value).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BucketUtil.Buckets.FILES.value).build());
            }

            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(BucketUtil.Buckets.NOTIFICATION.value).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BucketUtil.Buckets.NOTIFICATION.value).build());
            }
        } catch (Exception e) {
            log.error("Ошибка", e);
        }

    }
}
