package com.example.demo.controller;

import com.example.demo.models.Announcement;
import com.example.demo.models.Event;
import com.example.demo.models.Feedback;
import com.example.demo.models.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.service.AnnouncementService;
import com.example.demo.service.EventService;
import com.example.demo.service.FeedbackService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/organizers")
public class OrganizerController {

    @Autowired
    private EventService eventService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private UserService userService;

    @Autowired
    private FeedbackService feedbackService;

    // Get events created by this organizer
    @GetMapping("/my-events/{email}")
    public ResponseEntity<List<Event>> getMyEvents(@PathVariable String email) {
        Optional<User> organizer = userService.getUserByEmail(email);
        if (organizer.isEmpty() || !"Organizer".equals(organizer.get().getUserType())) {
            return ResponseEntity.badRequest().build();
        }
        List<Event> events = eventService.getEventsByOrganizer(organizer.get());
        return ResponseEntity.ok(events);
    }

    // Add an announcement for an event
    @PostMapping("/add-announcement/{eventId}")
    public ResponseEntity<String> addAnnouncement(@PathVariable Long eventId, @RequestBody String message) {
        Optional<Event> eventOptional = eventService.getEventById(eventId);
        if (eventOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Event not found.");
        }
        Event event = eventOptional.get();
        announcementService.saveAnnouncement(event, message);
        return ResponseEntity.ok("Announcement added successfully.");
    }

    // Get feedback report for an event
    @GetMapping("/feedback-report/{eventId}")
    public ResponseEntity<FeedbackReport> getFeedbackReport(@PathVariable Long eventId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByEventId(eventId);
        double avg = feedbacks.stream().mapToInt(Feedback::getRating).average().orElse(0.0);

        String remark;
        if (avg >= 4.5) remark = "Excellent";
        else if (avg >= 4.0) remark = "Good";
        else if (avg >= 3.0) remark = "Satisfactory";
        else if (avg >= 2.0) remark = "Needs Improvement";
        else remark = "Poor";

        FeedbackReport report = new FeedbackReport();
        report.setAverageRating(avg);
        report.setRemark(remark);
        report.setFeedbacks(feedbacks);

        return ResponseEntity.ok(report);
    }

    // FeedbackReport DTO class
    public static class FeedbackReport {
        private double averageRating;
        private String remark;
        private List<Feedback> feedbacks;

        public double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(double averageRating) {
            this.averageRating = averageRating;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public List<Feedback> getFeedbacks() {
            return feedbacks;
        }

        public void setFeedbacks(List<Feedback> feedbacks) {
            this.feedbacks = feedbacks;
        }
    }
}
