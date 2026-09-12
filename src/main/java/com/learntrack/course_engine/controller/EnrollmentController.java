package com.learntrack.course_engine.controller;

import com.learntrack.course_engine.entity.CourseEntity;
import com.learntrack.course_engine.service.EnrollmentService;
import com.learntrack.course_engine.service.EnrollmentService.EnrollmentResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<CourseEntity> createCourse(@RequestBody CourseEntity course) {
        CourseEntity created = enrollmentService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<EnrollmentResult> enrollStudent(@PathVariable("courseId") String courseId) {
        EnrollmentResult result = enrollmentService.enrollStudent(courseId);

        if (!result.isEnrolled()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
        }

        return ResponseEntity.ok(result);
    }
}