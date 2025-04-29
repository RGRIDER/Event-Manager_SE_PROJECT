package com.example.demo.controller;

import com.example.demo.models.Event;
import com.example.demo.models.Feedback;
import com.example.demo.models.Participant;
import com.example.demo.models.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.service.EventService;
import com.example.demo.service.ParticipantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private EventService eventService;

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test getFullEventReports() method
     */
    @Test
    void testGetFullEventReports() {
        // Arrange
        Event event = new Event();
        event.setEventId(1L);
        event.setTitle("Tech Conference");
        User organizer = new User();
        organizer.setFirstName("John");
        organizer.setLastName("Doe");
        organizer.setEmail("john.doe@example.com");
        event.setOrganizer(organizer);

        List<Event> events = Collections.singletonList(event);
        when(eventRepository.findAll()).thenReturn(events);

        Feedback feedback = new Feedback();
        feedback.setRating(5);
        List<Feedback> feedbacks = Collections.singletonList(feedback);
        when(feedbackRepository.findByParticipant_Event_EventId(1L)).thenReturn(feedbacks);

        Participant participant = new Participant();
        participant.setParticipantId(1L);
        List<Participant> participants = Collections.singletonList(participant);
        when(participantRepository.findByEvent(event)).thenReturn(participants);

        // Act
        ResponseEntity<List<AdminController.FullEventReport>> response = adminController.getFullEventReports();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        List<AdminController.FullEventReport> reports = response.getBody();
        assertNotNull(reports);
        assertEquals(1, reports.size());
        AdminController.FullEventReport report = reports.get(0);
        assertEquals(1L, report.getEventId());
        assertEquals("Tech Conference", report.getEventTitle());
        assertEquals("John Doe", report.getOrganizerName());
        assertEquals("john.doe@example.com", report.getOrganizerEmail());
        assertEquals(5.0, report.getAverageRating());
        assertEquals("Excellent", report.getRemark());
        assertEquals(1, report.getFeedbacks().size());
        assertEquals(1, report.getParticipants().size());

        verify(eventRepository, times(1)).findAll();
        verify(feedbackRepository, times(1)).findByParticipant_Event_EventId(1L);
        verify(participantRepository, times(1)).findByEvent(event);
    }

    /**
     * Test removeParticipant() - success case
     */
    @Test
    void testRemoveParticipant_Success() {
        // Arrange
        Participant participant = new Participant();
        participant.setParticipantId(1L);

        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));

        // Act
        ResponseEntity<String> response = adminController.removeParticipant(1L);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Participant removed from event.", response.getBody());

        verify(participantRepository, times(1)).findById(1L);
        verify(participantRepository, times(1)).delete(participant);
    }

    /**
     * Test removeParticipant() - participant not found case
     */
    @Test
    void testRemoveParticipant_NotFound() {
        // Arrange
        when(participantRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = adminController.removeParticipant(1L);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Participant not found.", response.getBody());

        verify(participantRepository, times(1)).findById(1L);
        verify(participantRepository, times(0)).delete(any());
    }
}
