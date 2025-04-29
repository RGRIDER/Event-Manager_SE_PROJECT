package com.example.demo.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    private Participant participant;
    private User user;
    private Event event;

    @BeforeEach
    void setUp() {
        user = new User();    // Mock simple User
        event = new Event();  // Mock simple Event
        participant = new Participant(user, event);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(user, participant.getUser());
        assertEquals(event, participant.getEvent());
        assertNull(participant.getParticipantId()); // ID should be null initially
    }

    @Test
    void testSetParticipantId() {
        participant.setParticipantId(100L);
        assertEquals(100L, participant.getParticipantId());
    }

    @Test
    void testSetUser() {
        User newUser = new User();
        participant.setUser(newUser);
        assertEquals(newUser, participant.getUser());
    }

    @Test
    void testSetEvent() {
        Event newEvent = new Event();
        participant.setEvent(newEvent);
        assertEquals(newEvent, participant.getEvent());
    }

    @Test
    void testDefaultConstructor() {
        Participant emptyParticipant = new Participant();
        assertNull(emptyParticipant.getParticipantId());
        assertNull(emptyParticipant.getUser());
        assertNull(emptyParticipant.getEvent());
    }
}
