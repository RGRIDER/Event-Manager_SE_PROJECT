package com.example.demo.controller;

import com.example.demo.models.Ticket;
import com.example.demo.models.User;
import com.example.demo.models.Event;
import com.example.demo.service.TicketService;
import com.example.demo.service.UserService;
import com.example.demo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @PostMapping("/book/{userEmail}/{eventId}")
    public ResponseEntity<Ticket> bookTicket(@PathVariable String userEmail, @PathVariable Long eventId, @RequestBody Ticket ticket) {
        Optional<User> user = userService.getUserByEmail(userEmail);
        Optional<Event> event = eventService.getEventById(eventId);

        if (user.isEmpty() || event.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(ticketService.bookTicket(user.get(), event.get(), ticket.getPrice(), "Booked"));
    }

    @DeleteMapping("/cancel/{ticketId}")
    public ResponseEntity<String> cancelTicket(@PathVariable Long ticketId) {
        ticketService.cancelTicket(ticketId);
        return ResponseEntity.ok("Ticket cancelled successfully!");
    }
}
