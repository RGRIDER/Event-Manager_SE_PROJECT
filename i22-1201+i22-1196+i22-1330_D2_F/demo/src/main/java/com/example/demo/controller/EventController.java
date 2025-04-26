package com.example.demo.controller;

import com.example.demo.models.Event;
import com.example.demo.models.User;
import com.example.demo.models.Announcement;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.service.AnnouncementService;
import com.example.demo.service.EventService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/my-events/{organizerEmail}")
    public ResponseEntity<?> getEventsByOrganizer(@PathVariable String organizerEmail) {
        Optional<User> organizer = userService.getUserByEmail(organizerEmail);

        if (organizer.isEmpty() || !"Organizer".equals(organizer.get().getUserType())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Unauthorized request."));
        }

        List<Event> events = eventService.getEventsByOrganizer(organizer.get());

        if (events.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No events found for this organizer."));
        }

        return ResponseEntity.ok(Map.of("events", events));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PostMapping("/announce/{organizerEmail}/{eventId}")
    public ResponseEntity<?> addAnnouncement(
            @PathVariable String organizerEmail,
            @PathVariable Long eventId,
            @RequestBody Map<String, String> body) {

        Optional<User> organizer = userService.getUserByEmail(organizerEmail);
        Optional<Event> eventOpt = eventService.getEventById(eventId);

        if (organizer.isEmpty() || eventOpt.isEmpty() || !"Organizer".equals(organizer.get().getUserType())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid organizer or event"));
        }

        Event event = eventOpt.get();
        if (!event.getOrganizer().getEmail().equals(organizerEmail)) {
            return ResponseEntity.badRequest().body(Map.of("error", "You can only announce for your own event"));
        }

        String message = body.get("message");
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Message cannot be empty"));
        }

        announcementService.saveAnnouncement(event, message);
        return ResponseEntity.ok(Map.of("message", "Announcement added successfully"));
    }
}
