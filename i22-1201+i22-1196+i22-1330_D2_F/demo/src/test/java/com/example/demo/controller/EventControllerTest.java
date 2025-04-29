package com.example.demo.controller;

import com.example.demo.models.Event;
import com.example.demo.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEvent() {
        Event event = new Event();
        String organizerEmail = "organizer@example.com";

        when(eventService.createEvent(event, organizerEmail)).thenReturn(event);

        ResponseEntity<Event> response = eventController.createEvent(event, organizerEmail);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(event, response.getBody());
        verify(eventService, times(1)).createEvent(event, organizerEmail);
    }

    @Test
    void testGetAllEvents() {
        List<Event> events = Arrays.asList(new Event(), new Event());
        when(eventService.getAllEvents()).thenReturn(events);

        ResponseEntity<List<Event>> response = eventController.getAllEvents();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(eventService, times(1)).getAllEvents();
    }

    @Test
    void testGetMyEvents() {
        String organizerEmail = "organizer@example.com";
        List<Event> events = Arrays.asList(new Event(), new Event());
        when(eventService.getEventsByOrganizerEmail(organizerEmail)).thenReturn(events);

        ResponseEntity<List<Event>> response = eventController.getMyEvents(organizerEmail);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(eventService, times(1)).getEventsByOrganizerEmail(organizerEmail);
    }

    @Test
    void testGetEventById_Found() {
        Long eventId = 1L;
        Event event = new Event();
        when(eventService.getEventById(eventId)).thenReturn(Optional.of(event));

        ResponseEntity<Event> response = eventController.getEventById(eventId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(event, response.getBody());
        verify(eventService, times(1)).getEventById(eventId);
    }

    @Test
    void testGetEventById_NotFound() {
        Long eventId = 1L;
        when(eventService.getEventById(eventId)).thenReturn(Optional.empty());

        ResponseEntity<Event> response = eventController.getEventById(eventId);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(eventService, times(1)).getEventById(eventId);
    }

    @Test
    void testUpdateEvent() {
        Long eventId = 1L;
        Event updatedEvent = new Event();
        when(eventService.updateEvent(eventId, updatedEvent)).thenReturn(updatedEvent);

        ResponseEntity<Event> response = eventController.updateEvent(eventId, updatedEvent);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedEvent, response.getBody());
        verify(eventService, times(1)).updateEvent(eventId, updatedEvent);
    }

    @Test
    void testDeleteEvent() {
        Long eventId = 1L;
        when(eventService.deleteEvent(eventId)).thenReturn(true);

        ResponseEntity<Boolean> response = eventController.deleteEvent(eventId);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(eventService, times(1)).deleteEvent(eventId);
    }

    @Test
    void testSearchEvents() {
        String title = "Sample Event";
        String organizerName = "John Doe";
        String location = "New York";

        List<Event> events = Arrays.asList(new Event(), new Event());
        when(eventService.searchEvents(title, organizerName, location)).thenReturn(events);

        ResponseEntity<List<Event>> response = eventController.searchEvents(title, organizerName, location);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(eventService, times(1)).searchEvents(title, organizerName, location);
    }
}
