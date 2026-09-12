package com.learntrack.course_engine.consumer;

import com.learntrack.course_engine.config.RabbitMQConfig;
import com.learntrack.course_engine.event.CourseWaitlistEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
public class WaitlistConsumer {

	@RabbitListener(queues = RabbitMQConfig.WAITLIST_QUEUE)
	public void processWaitlistMessage(CourseWaitlistEvent event) {
		System.out.printf("🐰 [RabbitMQ Listener] Received waitlist message for Student %s on Course %s%n", event.studentId(), event.courseId());
	}
}
