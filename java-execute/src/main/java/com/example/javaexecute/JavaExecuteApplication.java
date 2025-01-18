package com.example.javaexecute;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class JavaExecuteApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaExecuteApplication.class, args);
    }

}
