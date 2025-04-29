package com.example.demo.service;

import com.example.demo.models.Event;
import com.example.demo.models.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EventService eventService;

    private User organizer;
    private Event event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        organizer = new User();
        organizer.setUserId(1L);
        organizer.setFirstName("Ali");
        organizer.setLastName("Zia");
        organizer.setEmail("ali@example.com");
        organizer.setUserType("Organizer");

        event = new Event();
        event.setEventId(101L);
        event.setTitle("Spring Fest");
        event.setDescription("Annual university spring festival.");
        event.setDate(new Date()); // java.util.Date to avoid error
        event.setLocation("Campus Grounds");
    }

    @Test
    void testCreateEvent_withValidOrganizer() {
        when(userRepository.findByEmail(organizer.getEmail())).thenReturn(Optional.of(organizer));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event saved = eventService.createEvent(event, organizer.getEmail());

        assertEquals(event.getTitle(), saved.getTitle());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testCreateEvent_withInvalidOrganizer() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                eventService.createEvent(event, "unknown@example.com"));

        assertEquals("Invalid organizer email.", exception.getMessage());
    }

    @Test
    void testGetAllEvents() {
        when(eventRepository.findAll()).thenReturn(List.of(event));

        List<Event> events = eventService.getAllEvents();

        assertEquals(1, events.size());
        assertEquals("Spring Fest", events.get(0).getTitle());
    }

    @Test
    void testSearchEvents() {
        when(eventRepository.searchEvents("Spring", "Ali", "Campus")).thenReturn(List.of(event));

        List<Event> result = eventService.searchEvents("Spring", "Ali", "Campus");

        assertEquals(1, result.size());
        assertEquals("Spring Fest", result.get(0).getTitle());
    }

    @Test
    void testGetEventsByOrganizer() {
        when(eventRepository.findByOrganizer(organizer)).thenReturn(List.of(event));

        List<Event> result = eventService.getEventsByOrganizer(organizer);

        assertEquals(1, result.size());
    }

    @Test
    void testGetEventsByOrganizerEmail_valid() {
        when(userRepository.findByEmail(organizer.getEmail())).thenReturn(Optional.of(organizer));
        when(eventRepository.findByOrganizer(organizer)).thenReturn(List.of(event));

        List<Event> result = eventService.getEventsByOrganizerEmail(organizer.getEmail());

        assertEquals(1, result.size());
    }

    @Test
    void testGetEventsByOrganizerEmail_invalid() {
        when(userRepository.findByEmail("bad@example.com")).thenReturn(Optional.empty());

        List<Event> result = eventService.getEventsByOrganizerEmail("bad@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetEventById_found() {
        when(eventRepository.findById(101L)).thenReturn(Optional.of(event));

        Optional<Event> found = eventService.getEventById(101L);

        assertTrue(found.isPresent());
        assertEquals("Spring Fest", found.get().getTitle());
    }

    @Test
    void testDeleteEvent_successful() {
        when(eventRepository.existsById(101L)).thenReturn(true);

        boolean result = eventService.deleteEvent(101L);

        assertTrue(result);
        verify(eventRepository).deleteById(101L);
    }

    @Test
    void testDeleteEvent_notFound() {
        when(eventRepository.existsById(101L)).thenReturn(false);

        boolean result = eventService.deleteEvent(101L);

        assertFalse(result);
        verify(eventRepository, never()).deleteById(anyLong());
    }

    @Test
    void testUpdateEvent_successful() {
        Event updated = new Event();
        updated.setTitle("Updated Fest");
        updated.setDescription("Updated desc");
        updated.setDate(new Date());
        updated.setLocation("New Location");

        when(eventRepository.findById(101L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updated);

        Event result = eventService.updateEvent(101L, updated);

        assertEquals("Updated Fest", result.getTitle());
    }

    @Test
    void testUpdateEvent_notFound() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                eventService.updateEvent(999L, new Event()));

        assertEquals("Event not found with ID: 999", ex.getMessage());
    }
}
