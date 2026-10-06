package com.learntrack.course_engine.controller;

import com.learntrack.course_engine.entity.CourseEntity;
import com.learntrack.course_engine.service.EnrollmentService;
import com.learntrack.course_engine.service.EnrollmentService.EnrollmentResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "Course Enrollment Management", description = "Endpoints for creating courses and executing concurrent enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    @Operation(summary = "Create a new course", description = "Registers a course with specified seat capacity.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Course created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid payload or non-positive capacity")
    })
    public ResponseEntity<CourseEntity> createCourse(@Valid @RequestBody CourseEntity course) {
        CourseEntity created = enrollmentService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{courseId}/enroll")
    @Operation(summary = "Enroll student in course", description = "Concurrently enrolls a student. Handles race conditions with optimistic locking or routes to waitlist.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student successfully enrolled"),
        @ApiResponse(responseCode = "202", description = "Course capacity full; student added to waitlist queue"),
        @ApiResponse(responseCode = "429", description = "Too many requests (Rate limit exceeded)")
    })
    public ResponseEntity<EnrollmentResult> enrollStudent(@PathVariable String courseId) {
        EnrollmentResult result = enrollmentService.enrollStudent(courseId);
        if (result.isEnrolled()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
        }
    }
}