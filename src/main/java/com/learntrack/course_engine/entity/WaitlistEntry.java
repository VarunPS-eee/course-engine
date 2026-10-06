package com.learntrack.course_engine.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "waitlist_entries")
public class WaitlistEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseId;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WaitlistStatus status;

    public enum WaitlistStatus {
        PENDING,
        PROMOTED,
        CANCELLED
    }

    public WaitlistEntry() {}

    public WaitlistEntry(String courseId, String studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.createdAt = LocalDateTime.now();
        this.status = WaitlistStatus.PENDING;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getCourseId() { return courseId; }
    public String getStudentId() { return studentId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public WaitlistStatus getStatus() { return status; }

    public void setStatus(WaitlistStatus status) { this.status = status; }
}