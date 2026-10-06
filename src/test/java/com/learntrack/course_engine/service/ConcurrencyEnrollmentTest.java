package com.learntrack.course_engine.service;

import com.learntrack.course_engine.entity.CourseEntity;
import com.learntrack.course_engine.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class ConcurrencyEnrollmentTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
    }

    @Test
    @DisplayName("Simulate 50 concurrent requests for 5 capacity spots - verify zero over-enrollment")
    void concurrentEnrollment_ShouldPreventRaceConditions() throws InterruptedException {
        int capacity = 5;
        int totalThreads = 50;

        CourseEntity course = new CourseEntity("HIGH-DEMAND-101", "System Design Masterclass", capacity, 0);
        enrollmentService.createCourse(course);

        ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger successfulEnrollments = new AtomicInteger(0);
        AtomicInteger waitlistedCount = new AtomicInteger(0);

        for (int i = 0; i < totalThreads; i++) {
            executorService.submit(() -> {
                try {
                    latch.await(); // Hold all threads until signal
                    EnrollmentService.EnrollmentResult result = enrollmentService.enrollStudent("HIGH-DEMAND-101");
                    if (result.isEnrolled()) {
                        successfulEnrollments.incrementAndGet();
                    } else {
                        waitlistedCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    // Optimistic locking retries or rate limit exceptions
                }
            });
        }

        latch.countDown(); // Release all 50 threads at once
        executorService.shutdown();
        executorService.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS);

        CourseEntity updatedCourse = courseRepository.findById("HIGH-DEMAND-101").orElseThrow();

        System.out.println("📊 Concurrency Test Results:");
        System.out.println("  - Enrolled Count: " + updatedCourse.getEnrolledCount());
        System.out.println("  - Waitlisted Count: " + waitlistedCount.get());

        // Assert strictly that enrolled count NEVER exceeds course capacity
        assertEquals(capacity, updatedCourse.getEnrolledCount(), "Enrolled count must equal course capacity exactly");
    }
}