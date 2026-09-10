package com.learntrack.course_engine.service;

import org.springframework.stereotype.Service;

import com.learntrack.course_engine.entity.CourseEntity;
import com.learntrack.course_engine.repository.CourseRepository;


@Service 
public class EnrollmentService {
    private final CourseRepository courseRepository;

    //Contructor Injection
    public EnrollmentService(CourseRepository courseRepository)
    {
        this.courseRepository=courseRepository;
    }

    public CourseEntity createCourse(CourseEntity course)
    {
        return courseRepository.save(course);
    }
    public CourseEntity enrollStudent(String courseId)
    {
        CourseEntity course= courseRepository.findById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("Course not found with ID : "+ courseId));

            course.enroll();

            return courseRepository.save(course);

    }
}
