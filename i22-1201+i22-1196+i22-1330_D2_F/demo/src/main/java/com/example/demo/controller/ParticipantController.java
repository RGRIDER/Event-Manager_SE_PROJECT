package com.example.demo.controller;

import com.example.demo.models.Participant;
import com.example.demo.models.User;
import com.example.demo.models.Event;
import com.example.demo.models.Announcement;
import com.example.demo.service.ParticipantService;
import com.example.demo.service.UserService;
import com.example.demo.service.EventService;
import com.example.demo.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.AnnouncementRepository;

import java.util.ArrayList;
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

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private AnnouncementRepository announcementRepository;


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

    @GetMapping("/announcements/{email}")
    public ResponseEntity<?> getAnnouncementsForParticipant(@PathVariable String email) {
        List<Participant> participantEvents = participantService.getEventsByUser(email);
        List<Announcement> allAnnouncements = new ArrayList<>();

        for (Participant p : participantEvents) {
            List<Announcement> announcements = announcementRepository.findByEvent_EventIdOrderByTimestampDesc(p.getEvent().getEventId());
            allAnnouncements.addAll(announcements);
        }

        return ResponseEntity.ok(allAnnouncements);
    }


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

    // ✅ Updated: Now accepts rating
    @PostMapping("/feedback")
    public ResponseEntity<String> submitFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        Optional<Participant> participant = participantService.findByUserEmailAndEventId(
                feedbackRequest.getUsername(), feedbackRequest.getEventId());

        if (participant.isPresent()) {
            feedbackService.saveFeedback(
                    participant.get(),
                    feedbackRequest.getFeedback(),
                    feedbackRequest.getRating()
            );
            return ResponseEntity.ok("Feedback submitted successfully.");
        } else {
            return ResponseEntity.badRequest().body("Participant not found for this event.");
        }
    }

    // ✅ Updated DTO to include rating
    public static class FeedbackRequest {
        private String username;
        private Long eventId;
        private String feedback;
        private int rating;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public Long getEventId() { return eventId; }
        public void setEventId(Long eventId) { this.eventId = eventId; }

        public String getFeedback() { return feedback; }
        public void setFeedback(String feedback) { this.feedback = feedback; }

        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }
    }
}
