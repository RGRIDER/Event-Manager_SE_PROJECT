package com.example.demo.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AnnouncementTest {

    private Event mockEvent;
    private Announcement announcement;
    private LocalDateTime timestamp;

    @BeforeEach
    void setUp() {
        mockEvent = new Event(); // You can add fields if needed
        timestamp = LocalDateTime.now();
        announcement = new Announcement(mockEvent, "Test message", timestamp);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(mockEvent, announcement.getEvent());
        assertEquals("Test message", announcement.getMessage());
        assertEquals(timestamp, announcement.getTimestamp());
        assertNull(announcement.getId()); // ID should be null before persistence
    }

    @Test
    void testSetEvent() {
        Event newEvent = new Event();
        announcement.setEvent(newEvent);
        assertEquals(newEvent, announcement.getEvent());
    }

    @Test
    void testSetMessage() {
        announcement.setMessage("Updated message");
        assertEquals("Updated message", announcement.getMessage());
    }

    @Test
    void testSetTimestamp() {
        LocalDateTime newTimestamp = LocalDateTime.now().plusDays(1);
        announcement.setTimestamp(newTimestamp);
        assertEquals(newTimestamp, announcement.getTimestamp());
    }

    @Test
    void testDefaultConstructor() {
        Announcement empty = new Announcement();
        assertNull(empty.getId());
        assertNull(empty.getEvent());
        assertNull(empty.getMessage());
        assertNull(empty.getTimestamp());
    }
}
