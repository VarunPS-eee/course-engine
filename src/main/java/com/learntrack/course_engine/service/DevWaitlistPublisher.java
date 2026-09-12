package com.learntrack.course_engine.service;

import com.learntrack.course_engine.event.CourseWaitlistEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevWaitlistPublisher implements WaitlistPublisher {

    @Override
    public void publishWaitlistEvent(CourseWaitlistEvent event) {
        System.out.printf("🐰 [DEV Fallback Waitlist] Student %s added to waitlist for Course %s%n",
                event.studentId(), event.courseId());
    }
}