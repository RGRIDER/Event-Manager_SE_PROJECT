package com.example.demo.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    private Feedback feedback;
    private Participant participant;

    @BeforeEach
    void setUp() {
        participant = new Participant(); // Mocked simple Participant object
        feedback = new Feedback(participant, "Great event!", 5);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(participant, feedback.getParticipant());
        assertEquals("Great event!", feedback.getContent());
        assertEquals(5, feedback.getRating());
        assertNull(feedback.getId()); // ID should be null initially
    }

    @Test
    void testSetId() {
        feedback.setId(1L);
        assertEquals(1L, feedback.getId());
    }

    @Test
    void testSetParticipant() {
        Participant newParticipant = new Participant();
        feedback.setParticipant(newParticipant);
        assertEquals(newParticipant, feedback.getParticipant());
    }

    @Test
    void testSetContent() {
        feedback.setContent("Updated feedback content");
        assertEquals("Updated feedback content", feedback.getContent());
    }

    @Test
    void testSetRating() {
        feedback.setRating(4);
        assertEquals(4, feedback.getRating());
    }

    @Test
    void testDefaultConstructor() {
        Feedback emptyFeedback = new Feedback();
        assertNull(emptyFeedback.getId());
        assertNull(emptyFeedback.getParticipant());
        assertNull(emptyFeedback.getContent());
        assertEquals(0, emptyFeedback.getRating()); // int default = 0
    }
}
