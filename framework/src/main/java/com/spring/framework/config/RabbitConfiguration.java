package com.spring.framework.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class RabbitConfiguration {
    @Bean("emailQueue")
    public Queue emailQueue(){
        return QueueBuilder.
                durable("mail")
                .build();
    }
    @Bean
    public SimpleMessageConverter messageConverter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(Collections.singletonList("java.util.CollSer"));
        return converter;
    }

}
