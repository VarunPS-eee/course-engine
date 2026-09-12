package com.learntrack.course_engine.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String WAITLIST_QUEUE = "course.waitlist.queue";
	public static final String WAITLIST_EXCHANGE = "course.waitlist.exchange";
	public static final String WAITLIST_ROUTING_KEY = "course.waitlist.routingKey";

	@Bean
	@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
	public Queue waitlistQueue() {
		return QueueBuilder.durable(WAITLIST_QUEUE).build();
	}

	@Bean
	@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
	public TopicExchange waitlistExchange() {
		return new TopicExchange(WAITLIST_EXCHANGE);
	}

	@Bean
	@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
	public Binding binding(Queue waitlistQueue, TopicExchange waitlistExchange) {
		return BindingBuilder.bind(waitlistQueue).to(waitlistExchange).with(WAITLIST_ROUTING_KEY);
	}

	@Bean
	@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
