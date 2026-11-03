
package com.ecommerce.user_service.messaging;

import com.ecommerce.user_service.event.UserLoginEvent;
import com.ecommerce.user_service.event.UserRegisteredEvent;
import com.ecommerce.user_service.event.UserUpdatedEvent;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public UserEventPublisher(
            KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserRegistered(UserRegisteredEvent event) {
        kafkaTemplate.send(
                "user-registered",
                event.userId().toString(),
                event
        );
    }

    public void publishUserUpdated(UserUpdatedEvent event) {
        kafkaTemplate.send(
                "user-updated",
                event.userId().toString(),
                event
        );
    }

    public void publishUserLogin(UserLoginEvent event) {
        kafkaTemplate.send(
                "user-login",
                event.userId().toString(),
                event
        );
    }
}
