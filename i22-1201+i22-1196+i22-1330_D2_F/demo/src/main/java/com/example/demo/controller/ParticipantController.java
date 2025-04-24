package com.example.demo.controller;

import com.example.demo.models.Participant;
import com.example.demo.models.User;
import com.example.demo.models.Event;
import com.example.demo.service.ParticipantService;
import com.example.demo.service.UserService;
import com.example.demo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @PostMapping("/register/{userEmail}/{eventId}")
    public ResponseEntity<String> registerForEvent(@PathVariable String userEmail, @PathVariable Long eventId) {
        Optional<User> user = userService.getUserByEmail(userEmail);
        Optional<Event> event = eventService.getEventById(eventId);

        if (user.isEmpty() || event.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid user or event!");
        }

        boolean success = participantService.registerForEvent(user.get(), event.get());
        return success
                ? ResponseEntity.ok("User registered for event!")
                : ResponseEntity.badRequest().body("User is already registered!");
    }

    @GetMapping("/byEvent/{eventId}")
    public ResponseEntity<List<Participant>> getParticipantsByEvent(@PathVariable Long eventId) {
        Optional<Event> event = eventService.getEventById(eventId);
        return event.map(value -> ResponseEntity.ok(participantService.getParticipantsByEvent(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/byUser/{email}")
    public ResponseEntity<List<Participant>> getEnrolledEvents(@PathVariable String email) {
        List<Participant> participants = participantService.getEventsByUser(email);
        return ResponseEntity.ok(participants);
    }


    // UPDATED: Unregister a user using email and eventId instead of participantId
    @DeleteMapping("/unregister/{userEmail}/{eventId}")
    public ResponseEntity<String> unregisterFromEvent(@PathVariable String userEmail, @PathVariable Long eventId) {
        Optional<User> user = userService.getUserByEmail(userEmail);
        Optional<Event> event = eventService.getEventById(eventId);

        if (user.isEmpty() || event.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid user or event!");
        }

        boolean success = participantService.unregisterFromEvent(user.get(), event.get());
        return success
                ? ResponseEntity.ok("User unregistered from event!")
                : ResponseEntity.badRequest().body("User is not registered for this event!");
    }
}
