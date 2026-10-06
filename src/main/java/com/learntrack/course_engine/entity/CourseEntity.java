package com.learntrack.course_engine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "courses")
public class CourseEntity {

    @Id
    @NotBlank(message = "Course ID cannot be blank")
    private String id;

    @NotBlank(message = "Course title cannot be blank")
    private String title;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than zero")
    private Integer capacity;

    @Min(value = 0, message = "Enrolled count cannot be negative")
    private Integer enrolledCount = 0;

    @Version
    private Long version;

    public CourseEntity() {}

    public CourseEntity(String id, String title, Integer capacity, Integer enrolledCount) {
        this.id = id;
        this.title = title;
        this.capacity = capacity;
        this.enrolledCount = enrolledCount != null ? enrolledCount : 0;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getEnrolledCount() { return enrolledCount; }
    public void setEnrolledCount(Integer enrolledCount) { this.enrolledCount = enrolledCount; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}