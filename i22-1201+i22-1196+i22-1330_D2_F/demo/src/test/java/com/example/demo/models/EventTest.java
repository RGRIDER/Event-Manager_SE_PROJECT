package com.example.demo.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event event;
    private User organizer;
    private Date eventDate;

    @BeforeEach
    void setUp() {
        organizer = new User(); // Mock User object, assuming default constructor
        eventDate = new Date(); // Current date
        event = new Event("Sample Title", "Sample Description", eventDate, "Sample Location", organizer);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("Sample Title", event.getTitle());
        assertEquals("Sample Description", event.getDescription());
        assertEquals(eventDate, event.getDate());
        assertEquals("Sample Location", event.getLocation());
        assertEquals(organizer, event.getOrganizer());
        assertNull(event.getEventId()); // ID should be null initially
    }

    @Test
    void testSetEventId() {
        event.setEventId(1L);
        assertEquals(1L, event.getEventId());
    }

    @Test
    void testSetTitle() {
        event.setTitle("Updated Title");
        assertEquals("Updated Title", event.getTitle());
    }

    @Test
    void testSetDescription() {
        event.setDescription("Updated Description");
        assertEquals("Updated Description", event.getDescription());
    }

    @Test
    void testSetDate() {
        Date newDate = new Date(System.currentTimeMillis() + 86400000); // +1 day
        event.setDate(newDate);
        assertEquals(newDate, event.getDate());
    }

    @Test
    void testSetLocation() {
        event.setLocation("New Location");
        assertEquals("New Location", event.getLocation());
    }

    @Test
    void testSetOrganizer() {
        User newOrganizer = new User();
        event.setOrganizer(newOrganizer);
        assertEquals(newOrganizer, event.getOrganizer());
    }

    @Test
    void testSetParticipants() {
        Participant participant = new Participant();
        List<Participant> participants = List.of(participant);

        event.setParticipants(participants);
        assertEquals(participants, event.getParticipants());
    }

    @Test
    void testSetTickets() {
        Ticket ticket = new Ticket();
        List<Ticket> tickets = List.of(ticket);

        event.setTickets(tickets);
        assertEquals(tickets, event.getTickets());
    }

    @Test
    void testDefaultConstructor() {
        Event emptyEvent = new Event();
        assertNull(emptyEvent.getEventId());
        assertNull(emptyEvent.getTitle());
        assertNull(emptyEvent.getDescription());
        assertNull(emptyEvent.getDate());
        assertNull(emptyEvent.getLocation());
        assertNull(emptyEvent.getOrganizer());
        assertNull(emptyEvent.getParticipants());
        assertNull(emptyEvent.getTickets());
    }
}
