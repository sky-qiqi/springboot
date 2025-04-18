package org.example.system.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "logistics.exchange";
    public static final String QUEUE_NAME = "sms.notification.queue";
    public static final String ROUTING_KEY = "logistics.status.updated";

    @Bean
    public TopicExchange logisticsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue smsNotificationQueue() {
        return new Queue(QUEUE_NAME, true); // durable queue
    }

    @Bean
    public Binding binding(Queue smsNotificationQueue, TopicExchange logisticsExchange) {
        return BindingBuilder.bind(smsNotificationQueue).to(logisticsExchange).with(ROUTING_KEY);
    }
}