package com.example.demo.controller;

import com.example.demo.models.Event;
import com.example.demo.models.Feedback;
import com.example.demo.models.User;
import com.example.demo.service.AnnouncementService;
import com.example.demo.service.EventService;
import com.example.demo.service.FeedbackService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizerControllerTest {

    @InjectMocks
    private OrganizerController organizerController;

    @Mock
    private EventService eventService;

    @Mock
    private AnnouncementService announcementService;

    @Mock
    private UserService userService;

    @Mock
    private FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMyEvents_Success() {
        User organizer = new User();
        organizer.setEmail("organizer@example.com");
        organizer.setUserType("Organizer");

        when(userService.getUserByEmail("organizer@example.com")).thenReturn(Optional.of(organizer));
        when(eventService.getEventsByOrganizer(organizer)).thenReturn(Collections.singletonList(new Event()));

        ResponseEntity<List<Event>> response = organizerController.getMyEvents("organizer@example.com");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetMyEvents_InvalidOrganizer() {
        when(userService.getUserByEmail("invalid@example.com")).thenReturn(Optional.empty());

        ResponseEntity<List<Event>> response = organizerController.getMyEvents("invalid@example.com");

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testAddAnnouncement_Success() {
        Event event = new Event();
        when(eventService.getEventById(1L)).thenReturn(Optional.of(event));

        ResponseEntity<String> response = organizerController.addAnnouncement(1L, "New announcement!");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Announcement added successfully.", response.getBody());
        verify(announcementService, times(1)).saveAnnouncement(event, "New announcement!");
    }

    @Test
    void testAddAnnouncement_EventNotFound() {
        when(eventService.getEventById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = organizerController.addAnnouncement(1L, "New announcement!");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Event not found.", response.getBody());
    }

    @Test
    void testGetFeedbackReport() {
        Feedback feedback = new Feedback();
        feedback.setRating(5);
        when(feedbackService.getFeedbacksByEventId(1L)).thenReturn(Collections.singletonList(feedback));

        ResponseEntity<OrganizerController.FeedbackReport> response = organizerController.getFeedbackReport(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(5.0, response.getBody().getAverageRating());
        assertEquals("Excellent", response.getBody().getRemark());
    }
}
