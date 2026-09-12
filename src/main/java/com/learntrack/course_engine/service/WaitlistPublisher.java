package com.learntrack.course_engine.service;

import com.learntrack.course_engine.event.CourseWaitlistEvent;

public interface WaitlistPublisher {
    void publishWaitlistEvent(CourseWaitlistEvent event);
}