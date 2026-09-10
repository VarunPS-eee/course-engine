package com.learntrack.course_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learntrack.course_engine.entity.CourseEntity;

@Repository 
public interface CourseRepository extends JpaRepository<CourseEntity,String>{

} 
