package com.example.demo.controller;

import com.example.demo.models.Event;
import com.example.demo.models.User;
import com.example.demo.service.EventService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    /**
     * 🔹 Create Event - Only organizers can create events.
     */
    @PostMapping("/create/{organizerEmail}")
    public ResponseEntity<?> createEvent(@PathVariable String organizerEmail, @RequestBody Event event) {
        Optional<User> organizer = userService.getUserByEmail(organizerEmail);

        if (organizer.isEmpty() || !"Organizer".equals(organizer.get().getUserType())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only organizers can create events."));
        }

        if (!validateEventDetails(event)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid event details. Please check your inputs."));
        }

        if (eventService.existsByTitle(event.getTitle())) {
            return ResponseEntity.badRequest().body(Map.of("error", "An event with this title already exists."));
        }

        event.setOrganizer(organizer.get());
        Event savedEvent = eventService.createEvent(event);

        return ResponseEntity.ok(Map.of("message", "Event created successfully!", "event", savedEvent));
    }

    /**
     * 🔹 Modify Event - Only the event creator (organizer) can modify their event.
     */
    @PutMapping("/modify/{organizerEmail}/{eventId}")
    public ResponseEntity<?> modifyEvent(
            @PathVariable String organizerEmail,
            @PathVariable Long eventId,
            @RequestBody Event updatedEvent) {

        Optional<Event> existingEvent = eventService.getEventById(eventId);
        Optional<User> organizer = userService.getUserByEmail(organizerEmail);

        if (existingEvent.isEmpty() || organizer.isEmpty() || !existingEvent.get().getOrganizer().getEmail().equals(organizerEmail)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Unauthorized. You can only modify your own events."));
        }

        if (!validateEventDetails(updatedEvent)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid event details. Please check your inputs."));
        }

        updatedEvent.setEventId(eventId);
        updatedEvent.setOrganizer(organizer.get());
        Event savedEvent = eventService.updateEvent(updatedEvent);

        return ResponseEntity.ok(Map.of("message", "Event modified successfully!", "event", savedEvent));
    }

    /**
     * 🔹 Delete Event - Only the event creator (organizer) can delete their event.
     */
    @DeleteMapping("/delete/{organizerEmail}/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable String organizerEmail, @PathVariable Long eventId) {
        Optional<Event> event = eventService.getEventById(eventId);

        if (event.isEmpty() || !event.get().getOrganizer().getEmail().equals(organizerEmail)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Unauthorized. You can only delete your own events."));
        }

        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(Map.of("message", "Event deleted successfully!"));
    }

    /**
     * 🔹 Get Events by Organizer - Only shows the logged-in organizer’s events.
     */
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

    /**
     * 🔹 Helper method to validate event details.
     */
    private boolean validateEventDetails(Event event) {
        if (event.getTitle() == null || event.getTitle().isEmpty() ||
                event.getDescription() == null || event.getDescription().isEmpty() ||
                event.getLocation() == null || event.getLocation().isEmpty()) {
            return false;
        }

        LocalTime minTime = LocalTime.of(8, 0, 0);
        LocalTime maxTime = LocalTime.of(22, 0, 0);
        LocalTime startTime = LocalTime.of(9, 0, 0);
        LocalTime endTime = LocalTime.of(20, 0, 0);

        if (startTime.isBefore(minTime) || endTime.isAfter(maxTime) || !startTime.isBefore(endTime)) {
            return false;
        }

        return true;
    }
}
