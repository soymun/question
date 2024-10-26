package com.example.javaexecute;

import org.mdkt.compiler.InMemoryJavaCompiler;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableRabbit
public class JavaExecuteApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaExecuteApplication.class, args);
    }

}
