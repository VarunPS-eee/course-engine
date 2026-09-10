package com.learntrack.course_engine.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learntrack.course_engine.entity.CourseEntity;
import com.learntrack.course_engine.service.EnrollmentService;

@Controller 
@RequestMapping ("api/v1/courses")
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService)
    {
        this.enrollmentService= enrollmentService;
    }

    @PostMapping 
    public ResponseEntity<CourseEntity> createCourse(@RequestBody CourseEntity course)
    {
        CourseEntity created= enrollmentService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<CourseEntity> enrollStudent(@PathVariable String courseId)
    {
        CourseEntity updated= enrollmentService.enrollStudent(courseId);
        return ResponseEntity.ok(updated);
    }
}
