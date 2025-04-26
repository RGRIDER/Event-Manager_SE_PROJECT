package com.example.demo.repository;

import com.example.demo.models.Announcement;
import com.example.demo.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByEvent(Event event);
    List<Announcement> findByEvent_EventIdOrderByTimestampDesc(Long eventId);
}
