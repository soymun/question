package com.example.site.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("courses");
    }

    @Bean
    public Queue postgres(){
        return new Queue("postgresql-check", true);
    }

    @Bean
    public Queue mysql(){
        return new Queue("mysql-check", true);
    }

    @Bean
    public Queue resultQueue(){
        return new Queue("result", true);
    }

    @Bean
    public Queue completedCode(){
        return new Queue("completed-code", true);
    }

    @Bean
    public Queue schemaMySqlQueue(){
        return new Queue("mysql-schema", true);
    }

    @Bean
    public Queue executeMySqlQueue(){
        return new Queue("mysql-execute", true);
    }

    @Bean
    public Queue schemaQueue(){
        return new Queue("postgresql-schema", true);
    }

    @Bean
    public Queue executeQueue(){
        return new Queue("postgresql-execute", true);
    }

    @Bean
    public Queue javaQueue(){
        return new Queue("JAVA", true);
    }

    @Bean
    public Queue pythonQueue(){
        return new Queue("PYTHON", true);
    }

    @Bean
    public Binding resultBinding(){
        return BindingBuilder.bind(resultQueue()).to(directExchange()).with("result");
    }

    @Bean
    public Binding completedCodeBinding(){
        return BindingBuilder.bind(completedCode()).to(directExchange()).with("completed-code");
    }

    @Bean
    public Binding schemaBinding(){
        return BindingBuilder.bind(schemaQueue()).to(directExchange()).with("schema");
    }

    @Bean
    public Binding executeBinding(){
        return BindingBuilder.bind(executeQueue()).to(directExchange()).with("execute");
    }

    @Bean
    public Binding schemaMySqlBinding(){
        return BindingBuilder.bind(schemaQueue()).to(directExchange()).with("mysql-schema");
    }

    @Bean
    public Binding executeMySqlBinding(){
        return BindingBuilder.bind(executeQueue()).to(directExchange()).with("mysql-execute");
    }

    @Bean
    public Binding checkBinding(){
        return BindingBuilder.bind(postgres()).to(directExchange()).with("postgresql-check");
    }

    @Bean
    public Binding mysqlBinding(){
        return BindingBuilder.bind(mysql()).to(directExchange()).with("mysql-check");
    }

    @Bean
    public Binding javaBinding(){
        return BindingBuilder.bind(javaQueue()).to(directExchange()).with("java");
    }

    @Bean
    public Binding pythonBinding(){
        return BindingBuilder.bind(pythonQueue()).to(directExchange()).with("python");
    }
}
