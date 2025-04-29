package com.example.demo.controller;

import com.example.demo.models.Ticket;
import com.example.demo.models.User;
import com.example.demo.models.Event;
import com.example.demo.service.TicketService;
import com.example.demo.service.UserService;
import com.example.demo.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private UserService userService;

    @Mock
    private EventService eventService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookTicket_Success() {
        // Arrange
        String userEmail = "test@example.com";
        Long eventId = 1L;
        double price = 50.0;

        User mockUser = new User();
        Event mockEvent = new Event();
        Ticket mockTicket = new Ticket();
        mockTicket.setPrice(price);

        when(userService.getUserByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        when(eventService.getEventById(eventId)).thenReturn(Optional.of(mockEvent));
        when(ticketService.bookTicket(mockUser, mockEvent, price, "Booked")).thenReturn(mockTicket);

        // Act
        ResponseEntity<Ticket> response = ticketController.bookTicket(userEmail, eventId, mockTicket);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockTicket, response.getBody());

        verify(userService, times(1)).getUserByEmail(userEmail);
        verify(eventService, times(1)).getEventById(eventId);
        verify(ticketService, times(1)).bookTicket(mockUser, mockEvent, price, "Booked");
    }

    @Test
    void testBookTicket_UserOrEventNotFound() {
        // Arrange
        String userEmail = "missing@example.com";
        Long eventId = 2L;
        Ticket dummyTicket = new Ticket();
        dummyTicket.setPrice(100.0);

        when(userService.getUserByEmail(userEmail)).thenReturn(Optional.empty());
        when(eventService.getEventById(eventId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Ticket> response = ticketController.bookTicket(userEmail, eventId, dummyTicket);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(userService, times(1)).getUserByEmail(userEmail);
        verify(eventService, times(1)).getEventById(eventId);
        verify(ticketService, never()).bookTicket(any(), any(), anyDouble(), anyString());
    }

    @Test
    void testCancelTicket_Success() {
        // Arrange
        Long ticketId = 1L;

        // Act
        ResponseEntity<String> response = ticketController.cancelTicket(ticketId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Ticket cancelled successfully!", response.getBody());

        verify(ticketService, times(1)).cancelTicket(ticketId);
    }
}
