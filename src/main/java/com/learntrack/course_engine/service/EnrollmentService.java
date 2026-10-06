package com.learntrack.course_engine.service;

import com.learntrack.course_engine.entity.CourseEntity;
import com.learntrack.course_engine.event.CourseWaitlistEvent;
import com.learntrack.course_engine.repository.CourseRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EnrollmentService {

    private final CourseRepository courseRepository;
    private final WaitlistPublisher waitlistPublisher;

    public EnrollmentService(CourseRepository courseRepository, WaitlistPublisher waitlistPublisher) {
        this.courseRepository = courseRepository;
        this.waitlistPublisher = waitlistPublisher;
    }

    public CourseEntity createCourse(CourseEntity course) {
        return courseRepository.save(course);
    }

    @Transactional
    @RateLimiter(name = "enrollmentRateLimiter")
    @Retryable(
        retryFor = { ObjectOptimisticLockingFailureException.class },
        maxAttempts = 10,
        backoff = @Backoff(delay = 50, maxDelay = 200, multiplier = 1.5)
    )
    public EnrollmentResult enrollStudent(String courseId) {
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        if (course.getEnrolledCount() >= course.getCapacity()) {
            String studentId = "STD-" + UUID.randomUUID().toString().substring(0, 8);
            CourseWaitlistEvent event = new CourseWaitlistEvent(courseId, studentId, System.currentTimeMillis());

            waitlistPublisher.publishWaitlistEvent(event);

            return new EnrollmentResult(false, "Course full. Added to waitlist queue.", course);
        }

        course.setEnrolledCount(course.getEnrolledCount() + 1);
        CourseEntity updatedCourse = courseRepository.save(course);

        return new EnrollmentResult(true, "Enrollment successful!", updatedCourse);
    }

    public record EnrollmentResult(boolean isEnrolled, String message, CourseEntity course) {}
}