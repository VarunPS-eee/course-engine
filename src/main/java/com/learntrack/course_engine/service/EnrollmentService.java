package com.learntrack.course_engine.service;

import com.learntrack.course_engine.entity.CourseEntity;
import com.learntrack.course_engine.repository.CourseRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnrollmentService {

    private final CourseRepository courseRepository;

    // Notice: Clean constructor injection with NO Redisson dependency
    public EnrollmentService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseEntity createCourse(CourseEntity course) {
        return courseRepository.save(course);
    }

    @Transactional
    @RateLimiter(name = "enrollmentRateLimiter")
    public CourseEntity enrollStudent(String courseId) {
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        if (course.getEnrolledCount() >= course.getCapacity()) {
            throw new IllegalStateException("Course capacity reached");
        }

        course.setEnrolledCount(course.getEnrolledCount() + 1);
        return courseRepository.save(course);
    }
}