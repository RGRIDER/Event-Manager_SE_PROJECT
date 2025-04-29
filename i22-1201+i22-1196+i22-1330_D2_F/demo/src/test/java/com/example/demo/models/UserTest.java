package com.example.demo.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Haider", "Zia", "haider@example.com", "securePass", "admin");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("Haider", user.getFirstName());
        assertEquals("Zia", user.getLastName());
        assertEquals("haider@example.com", user.getEmail());
        assertEquals("securePass", user.getPassword());
        assertEquals("admin", user.getUserType());
        assertNull(user.getUserId()); // Not persisted yet
    }

    @Test
    void testSetUserId() {
        user.setUserId(1L);
        assertEquals(1L, user.getUserId());
    }

    @Test
    void testSetFirstName() {
        user.setFirstName("Ali");
        assertEquals("Ali", user.getFirstName());
    }

    @Test
    void testSetLastName() {
        user.setLastName("Khan");
        assertEquals("Khan", user.getLastName());
    }

    @Test
    void testSetEmail() {
        user.setEmail("ali@example.com");
        assertEquals("ali@example.com", user.getEmail());
    }

    @Test
    void testSetPassword() {
        user.setPassword("newPass");
        assertEquals("newPass", user.getPassword());
    }

    @Test
    void testSetUserType() {
        user.setUserType("participant");
        assertEquals("participant", user.getUserType());
    }

    @Test
    void testEventsOrganizedList() {
        Event e1 = new Event();
        Event e2 = new Event();
        user.setEventsOrganized(List.of(e1, e2));
        assertEquals(2, user.getEventsOrganized().size());
        assertTrue(user.getEventsOrganized().contains(e1));
        assertTrue(user.getEventsOrganized().contains(e2));
    }

    @Test
    void testParticipantsList() {
        Participant p1 = new Participant();
        Participant p2 = new Participant();
        user.setParticipants(List.of(p1, p2));
        assertEquals(2, user.getParticipants().size());
        assertTrue(user.getParticipants().contains(p1));
        assertTrue(user.getParticipants().contains(p2));
    }

    @Test
    void testTicketsList() {
        Ticket t1 = new Ticket();
        Ticket t2 = new Ticket();
        user.setTickets(List.of(t1, t2));
        assertEquals(2, user.getTickets().size());
        assertTrue(user.getTickets().contains(t1));
        assertTrue(user.getTickets().contains(t2));
    }

    @Test
    void testDefaultConstructor() {
        User newUser = new User();
        assertNull(newUser.getUserId());
        assertNull(newUser.getFirstName());
        assertNull(newUser.getLastName());
        assertNull(newUser.getEmail());
        assertNull(newUser.getPassword());
        assertNull(newUser.getUserType());
        assertNull(newUser.getEventsOrganized());
        assertNull(newUser.getParticipants());
        assertNull(newUser.getTickets());
    }
}
