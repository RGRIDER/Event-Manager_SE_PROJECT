package com.example.demo.service;

import com.example.demo.models.Announcement;
import com.example.demo.models.Event;
import com.example.demo.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    public Announcement saveAnnouncement(Event event, String message) {
        Announcement announcement = new Announcement(event, message, LocalDateTime.now());
        return announcementRepository.save(announcement);
    }

    public List<Announcement> getAnnouncementsByEvent(Event event) {
        return announcementRepository.findByEvent(event);
    }

    public List<Announcement> getAnnouncementsByEventIdDesc(Long eventId) {
        return announcementRepository.findByEvent_EventIdOrderByTimestampDesc(eventId);
    }
}
