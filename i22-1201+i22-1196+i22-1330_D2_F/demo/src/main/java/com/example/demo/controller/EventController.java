package com.example.demo.controller;

import com.example.demo.models.Event;
import com.example.demo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/create/{organizerEmail}")
    public ResponseEntity<Event> createEvent(@RequestBody Event event, @PathVariable String organizerEmail) {
        Event createdEvent = eventService.createEvent(event, organizerEmail);
        return ResponseEntity.ok(createdEvent);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/my-events/{organizerEmail}")
    public ResponseEntity<List<Event>> getMyEvents(@PathVariable String organizerEmail) {
        List<Event> events = eventService.getEventsByOrganizerEmail(organizerEmail);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        Optional<Event> event = eventService.getEventById(eventId);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody Event updatedEvent) {
        Event event = eventService.updateEvent(eventId, updatedEvent);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Boolean> deleteEvent(@PathVariable Long eventId) {
        boolean deleted = eventService.deleteEvent(eventId);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String organizerName,
            @RequestParam(required = false) String location
    ) {
        List<Event> events = eventService.searchEvents(title, organizerName, location);
        return ResponseEntity.ok(events);
    }
}
