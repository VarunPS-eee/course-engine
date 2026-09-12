package com.learntrack.course_engine.service;

import com.learntrack.course_engine.config.RabbitMQConfig;
import com.learntrack.course_engine.event.CourseWaitlistEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!dev")
public class RabbitMQWaitlistPublisher implements WaitlistPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQWaitlistPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishWaitlistEvent(CourseWaitlistEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.WAITLIST_EXCHANGE,
                RabbitMQConfig.WAITLIST_ROUTING_KEY,
                event
        );
    }
}