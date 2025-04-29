package com.example.demo.controller;

import com.example.demo.models.Announcement;
import com.example.demo.models.Event;
import com.example.demo.models.Participant;
import com.example.demo.models.User;
import com.example.demo.service.ParticipantService;
import com.example.demo.service.UserService;
import com.example.demo.service.EventService;
import com.example.demo.service.FeedbackService;
import com.example.demo.repository.AnnouncementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParticipantControllerTest {

    @InjectMocks
    private ParticipantController participantController;

    @Mock
    private ParticipantService participantService;

    @Mock
    private UserService userService;

    @Mock
    private EventService eventService;

    @Mock
    private FeedbackService feedbackService;

    @Mock
    private AnnouncementRepository announcementRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterForEvent_Success() {
        User user = new User();
        Event event = new Event();

        when(userService.getUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(eventService.getEventById(1L)).thenReturn(Optional.of(event));
        when(participantService.registerForEvent(user, event)).thenReturn(true);

        ResponseEntity<String> response = participantController.registerForEvent("user@example.com", 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered for event!", response.getBody());
    }

    @Test
    void testRegisterForEvent_InvalidUserOrEvent() {
        when(userService.getUserByEmail("user@example.com")).thenReturn(Optional.empty());

        ResponseEntity<String> response = participantController.registerForEvent("user@example.com", 1L);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid user or event!", response.getBody());
    }

    @Test
    void testRegisterForEvent_AlreadyRegistered() {
        User user = new User();
        Event event = new Event();

        when(userService.getUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(eventService.getEventById(1L)).thenReturn(Optional.of(event));
        when(participantService.registerForEvent(user, event)).thenReturn(false);

        ResponseEntity<String> response = participantController.registerForEvent("user@example.com", 1L);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User is already registered!", response.getBody());
    }

    @Test
    void testGetParticipantsByEvent_Found() {
        Event event = new Event();
        List<Participant> participants = List.of(new Participant());

        when(eventService.getEventById(1L)).thenReturn(Optional.of(event));
        when(participantService.getParticipantsByEvent(event)).thenReturn(participants);

        ResponseEntity<List<Participant>> response = participantController.getParticipantsByEvent(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(participants, response.getBody());
    }

    @Test
    void testGetParticipantsByEvent_NotFound() {
        when(eventService.getEventById(1L)).thenReturn(Optional.empty());

        ResponseEntity<List<Participant>> response = participantController.getParticipantsByEvent(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetEnrolledEvents() {
        List<Participant> participants = List.of(new Participant());

        when(participantService.getEventsByUser("user@example.com")).thenReturn(participants);

        ResponseEntity<List<Participant>> response = participantController.getEnrolledEvents("user@example.com");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(participants, response.getBody());
    }

    @Test
    void testGetAnnouncementsForParticipant() {
        Participant participant = new Participant();
        Event event = new Event();
        event.setEventId(1L);
        participant.setEvent(event);

        List<Participant> participants = List.of(participant);
        List<Announcement> announcements = List.of(new Announcement());

        when(participantService.getEventsByUser("user@example.com")).thenReturn(participants);
        when(announcementRepository.findByEvent_EventIdOrderByTimestampDesc(1L)).thenReturn(announcements);

        ResponseEntity<?> response = participantController.getAnnouncementsForParticipant("user@example.com");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List);
        assertEquals(1, ((List<?>) response.getBody()).size());
    }

    @Test
    void testUnregisterFromEvent_Success() {
        User user = new User();
        Event event = new Event();

        when(userService.getUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(eventService.getEventById(1L)).thenReturn(Optional.of(event));
        when(participantService.unregisterFromEvent(user, event)).thenReturn(true);

        ResponseEntity<String> response = participantController.unregisterFromEvent("user@example.com", 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User unregistered from event!", response.getBody());
    }

    @Test
    void testUnregisterFromEvent_NotRegistered() {
        User user = new User();
        Event event = new Event();

        when(userService.getUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(eventService.getEventById(1L)).thenReturn(Optional.of(event));
        when(participantService.unregisterFromEvent(user, event)).thenReturn(false);

        ResponseEntity<String> response = participantController.unregisterFromEvent("user@example.com", 1L);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User is not registered for this event!", response.getBody());
    }

    @Test
    void testUnregisterFromEvent_InvalidUserOrEvent() {
        when(userService.getUserByEmail("user@example.com")).thenReturn(Optional.empty());

        ResponseEntity<String> response = participantController.unregisterFromEvent("user@example.com", 1L);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid user or event!", response.getBody());
    }

    @Test
    void testSubmitFeedback_Success() {
        Participant participant = new Participant();
        ParticipantController.FeedbackRequest request = new ParticipantController.FeedbackRequest();
        request.setUsername("user@example.com");
        request.setEventId(1L);
        request.setFeedback("Great event!");
        request.setRating(5);

        when(participantService.findByUserEmailAndEventId("user@example.com", 1L)).thenReturn(Optional.of(participant));

        ResponseEntity<String> response = participantController.submitFeedback(request);

        verify(feedbackService, times(1)).saveFeedback(participant, "Great event!", 5);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Feedback submitted successfully.", response.getBody());
    }

    @Test
    void testSubmitFeedback_ParticipantNotFound() {
        ParticipantController.FeedbackRequest request = new ParticipantController.FeedbackRequest();
        request.setUsername("user@example.com");
        request.setEventId(1L);

        when(participantService.findByUserEmailAndEventId("user@example.com", 1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = participantController.submitFeedback(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Participant not found for this event.", response.getBody());
    }
}
