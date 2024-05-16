package com.example.executesqlscriptsmicroservice.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
public class JpaConfig {

    @Autowired
    private EntityManagerFactory emf;

    @Bean
    public JpaTransactionManager transactionManager() {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
