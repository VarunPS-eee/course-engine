package com.learntrack.course_engine.service;

import com.learntrack.course_engine.entity.CourseEntity;
import com.learntrack.course_engine.repository.CourseRepository;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "resilience4j.ratelimiter.instances.enrollmentRateLimiter.limitForPeriod=2",
    "resilience4j.ratelimiter.instances.enrollmentRateLimiter.limitRefreshPeriod=10s",
    "resilience4j.ratelimiter.instances.enrollmentRateLimiter.timeoutDuration=0ms"
})
class RateLimiterTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
    }

    @Test
    @DisplayName("Should throw RequestNotPermitted when rate limit is exceeded")
    void enrollStudent_RateLimitExceeded() {
        CourseEntity course = new CourseEntity("RL-101", "Rate Limiting Course", 100, 0);
        enrollmentService.createCourse(course);

        // Allowed call 1
        enrollmentService.enrollStudent("RL-101");
        // Allowed call 2
        enrollmentService.enrollStudent("RL-101");

        // 3rd call exceeds limit of 2 -> throws RequestNotPermitted
        assertThrows(RequestNotPermitted.class, () -> enrollmentService.enrollStudent("RL-101"));
    }
}