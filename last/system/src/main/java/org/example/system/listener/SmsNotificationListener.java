package org.example.system.listener;

import org.example.system.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SmsNotificationListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleSmsNotification(String message) {
        System.out.println("Received message from queue: " + message);
        // 在这里添加实际的短信发送逻辑
        // 例如，调用第三方短信服务API
        System.out.println("Simulating sending SMS for: " + message);
    }
}