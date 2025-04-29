package com.example.demo.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    private Ticket ticket;
    private Event event;
    private User user;

    @BeforeEach
    void setUp() {
        event = new Event(); // Simple Event mock
        user = new User();   // Simple User mock
        ticket = new Ticket(event, user, 50.0, "Booked");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(event, ticket.getEvent());
        assertEquals(user, ticket.getUser());
        assertEquals(50.0, ticket.getPrice());
        assertEquals("Booked", ticket.getStatus());
        assertNull(ticket.getTicketId()); // ID should be null before persistence
    }

    @Test
    void testSetTicketId() {
        ticket.setTicketId(1L);
        assertEquals(1L, ticket.getTicketId());
    }

    @Test
    void testSetEvent() {
        Event newEvent = new Event();
        ticket.setEvent(newEvent);
        assertEquals(newEvent, ticket.getEvent());
    }

    @Test
    void testSetUser() {
        User newUser = new User();
        ticket.setUser(newUser);
        assertEquals(newUser, ticket.getUser());
    }

    @Test
    void testSetPrice() {
        ticket.setPrice(99.99);
        assertEquals(99.99, ticket.getPrice());
    }

    @Test
    void testSetStatus() {
        ticket.setStatus("Cancelled");
        assertEquals("Cancelled", ticket.getStatus());
    }

    @Test
    void testDefaultConstructor() {
        Ticket emptyTicket = new Ticket();
        assertNull(emptyTicket.getTicketId());
        assertNull(emptyTicket.getEvent());
        assertNull(emptyTicket.getUser());
        assertEquals(0.0, emptyTicket.getPrice());
        assertNull(emptyTicket.getStatus());
    }
}
