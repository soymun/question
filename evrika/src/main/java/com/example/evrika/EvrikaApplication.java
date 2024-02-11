package com.example.evrika;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EvrikaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvrikaApplication.class, args);
    }

}
