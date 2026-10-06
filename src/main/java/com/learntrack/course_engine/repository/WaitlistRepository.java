package com.learntrack.course_engine.repository;

import com.learntrack.course_engine.entity.WaitlistEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitlistRepository extends JpaRepository<WaitlistEntry, Long> {
    List<WaitlistEntry> findByCourseIdAndStatusOrderByCreatedAtAsc(String courseId, WaitlistEntry.WaitlistStatus status);
}