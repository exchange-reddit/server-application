package com.omniversity.public_community_service.PostDependency.KafkaConsumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    @KafkaListener(topics = "post-section", groupId = "post-dependency")
    public void listen (String message) {
        System.out.println(message);
    }
}
