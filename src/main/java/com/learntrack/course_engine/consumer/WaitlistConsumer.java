package com.learntrack.course_engine.consumer;

import com.learntrack.course_engine.entity.WaitlistEntry;
import com.learntrack.course_engine.event.CourseWaitlistEvent;
import com.learntrack.course_engine.repository.WaitlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class WaitlistConsumer {

    private static final Logger log = LoggerFactory.getLogger(WaitlistConsumer.class);

    private final WaitlistRepository waitlistRepository;

    public WaitlistConsumer(WaitlistRepository waitlistRepository) {
        this.waitlistRepository = waitlistRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void processWaitlistMessage(CourseWaitlistEvent event) {
        log.info("🐰 [Waitlist Consumer] Processing waitlist entry for Student {} on Course {}",
                event.studentId(), event.courseId());

        WaitlistEntry entry = new WaitlistEntry(event.courseId(), event.studentId());
        WaitlistEntry saved = waitlistRepository.save(entry);

        log.info("✅ [Waitlist Consumer] Persisted Waitlist Entry ID #{} into Database (Status: PENDING)", saved.getId());
    }
}