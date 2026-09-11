package com.learntrack.course_engine.entity;

import jakarta.persistence.*;

@Entity 
@Table(name="courses")
public class CourseEntity {
    // I need ID, title, enrolledCount, capacity and verison for locking
    @Id 
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int enrolledCount;

    @Version 
    private Long version;

    public CourseEntity(){}

    public CourseEntity(String id, String title, int capacity) {
        this.id = id;
        this.title = title;
        this.capacity = capacity;
        this.enrolledCount = 0;
    }

    public void enroll()
    {
        if(this.enrolledCount>=this.capacity)
        {
            throw new IllegalStateException("Course is already at full capacity");
        }
        this.enrolledCount++;
    }
    public String getId() {
        return id;
    }
    public int getCapacity() {
        return capacity;
    }
    public int getEnrolledCount() {
        return enrolledCount;
    }
    public String getTitle() {
        return title;
    }
    public Long getVersion() {
        return version;
    }
    public void setEnrolledCount(int enrolledCount) {
        this.enrolledCount = enrolledCount;
    }


}
