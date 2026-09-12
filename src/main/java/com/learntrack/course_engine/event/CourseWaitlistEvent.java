package com.learntrack.course_engine.event;

import java.io.Serializable;

public record CourseWaitlistEvent(String courseId, String studentId, long timestamp) implements Serializable {}
