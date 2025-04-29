package com.example.demo.service;

import com.example.demo.models.Announcement;
import com.example.demo.models.Event;
import com.example.demo.repository.AnnouncementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnnouncementServiceTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @InjectMocks
    private AnnouncementService announcementService;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testEvent = new Event();
        testEvent.setEventId(1L);
    }

    @Test
    void testSaveAnnouncement() {
        String message = "Event is postponed!";
        Announcement announcement = new Announcement(testEvent, message, LocalDateTime.now());

        when(announcementRepository.save(any(Announcement.class))).thenReturn(announcement);

        Announcement saved = announcementService.saveAnnouncement(testEvent, message);

        assertNotNull(saved);
        assertEquals(message, saved.getMessage());
        assertEquals(testEvent, saved.getEvent());
        assertNotNull(saved.getTimestamp());
        verify(announcementRepository, times(1)).save(any(Announcement.class));
    }

    @Test
    void testGetAnnouncementsByEvent() {
        List<Announcement> mockList = List.of(
                new Announcement(testEvent, "Msg 1", LocalDateTime.now()),
                new Announcement(testEvent, "Msg 2", LocalDateTime.now())
        );

        when(announcementRepository.findByEvent(testEvent)).thenReturn(mockList);

        List<Announcement> result = announcementService.getAnnouncementsByEvent(testEvent);

        assertEquals(2, result.size());
        verify(announcementRepository, times(1)).findByEvent(testEvent);
    }

    @Test
    void testGetAnnouncementsByEventIdDesc() {
        Long eventId = 1L;

        List<Announcement> mockList = List.of(
                new Announcement(testEvent, "Latest update", LocalDateTime.now()),
                new Announcement(testEvent, "Earlier update", LocalDateTime.now().minusHours(1))
        );

        when(announcementRepository.findByEvent_EventIdOrderByTimestampDesc(eventId)).thenReturn(mockList);

        List<Announcement> result = announcementService.getAnnouncementsByEventIdDesc(eventId);

        assertEquals(2, result.size());
        assertEquals("Latest update", result.get(0).getMessage());
        verify(announcementRepository, times(1)).findByEvent_EventIdOrderByTimestampDesc(eventId);
    }
}
