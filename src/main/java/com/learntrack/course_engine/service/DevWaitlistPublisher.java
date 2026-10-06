package com.learntrack.course_engine.service;

import com.learntrack.course_engine.event.CourseWaitlistEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev","test"})
public class DevWaitlistPublisher implements WaitlistPublisher {

    private static final Logger log = LoggerFactory.getLogger(DevWaitlistPublisher.class);
    private final ApplicationEventPublisher eventPublisher;

    public DevWaitlistPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publishWaitlistEvent(CourseWaitlistEvent event) {
        log.info("🐰 [DEV Fallback Waitlist] Publishing internal event for Student {} on Course {}",
                event.studentId(), event.courseId());
        eventPublisher.publishEvent(event);
    }
}