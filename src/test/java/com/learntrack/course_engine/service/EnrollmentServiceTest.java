package com.learntrack.course_engine.service;

import com.learntrack.course_engine.entity.CourseEntity;
import com.learntrack.course_engine.repository.CourseRepository;
import com.learntrack.course_engine.repository.WaitlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EnrollmentServiceTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private WaitlistRepository waitlistRepository;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        waitlistRepository.deleteAll();
    }

    @Test
    @DisplayName("Should successfully enroll student when capacity is available")
    void enrollStudent_Success() {
        CourseEntity course = new CourseEntity("TEST-101", "Unit Testing in Spring", 5, 0);
        enrollmentService.createCourse(course);

        EnrollmentService.EnrollmentResult result = enrollmentService.enrollStudent("TEST-101");

        assertTrue(result.isEnrolled());
        assertEquals(1, result.course().getEnrolledCount());
    }

    @Test
    @DisplayName("Should redirect to waitlist when course capacity is reached")
    void enrollStudent_WaitlistFallback() {
        CourseEntity course = new CourseEntity("TEST-102", "Microservices Architecture", 1, 0);
        enrollmentService.createCourse(course);

        // First student fills capacity
        EnrollmentService.EnrollmentResult result1 = enrollmentService.enrollStudent("TEST-102");
        assertTrue(result1.isEnrolled());

        // Second student triggers waitlist flow
        EnrollmentService.EnrollmentResult result2 = enrollmentService.enrollStudent("TEST-102");
        assertFalse(result2.isEnrolled());
        assertEquals("Course full. Added to waitlist queue.", result2.message());
    }
}