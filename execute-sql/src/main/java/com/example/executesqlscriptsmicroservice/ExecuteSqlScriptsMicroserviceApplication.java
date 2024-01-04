package com.example.executesqlscriptsmicroservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class ExecuteSqlScriptsMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExecuteSqlScriptsMicroserviceApplication.class, args);
    }

}
