package com.example.demo.controller;

import com.example.demo.models.Event;
import com.example.demo.models.Feedback;
import com.example.demo.models.Participant;
import com.example.demo.models.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.service.EventService;
import com.example.demo.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private ParticipantService participantService;

    /**
     * 🔹 Get All Events with Organizer and Average Feedback
     */
    @GetMapping("/full-report")
    public ResponseEntity<List<FullEventReport>> getFullEventReports() {
        List<Event> events = eventRepository.findAll();
        List<FullEventReport> reports = new ArrayList<>();

        for (Event event : events) {
            List<Feedback> feedbacks = feedbackRepository.findByParticipant_Event_EventId(event.getEventId());
            double avgRating = feedbacks.stream().mapToInt(Feedback::getRating).average().orElse(0.0);

            String remark;
            if (avgRating >= 4.5) remark = "Excellent";
            else if (avgRating >= 4.0) remark = "Good";
            else if (avgRating >= 3.0) remark = "Satisfactory";
            else if (avgRating >= 2.0) remark = "Needs Improvement";
            else remark = "Poor";

            FullEventReport report = new FullEventReport();
            report.setEventId(event.getEventId());
            report.setEventTitle(event.getTitle());
            report.setOrganizerName(event.getOrganizer().getFirstName() + " " + event.getOrganizer().getLastName());
            report.setOrganizerEmail(event.getOrganizer().getEmail());
            report.setAverageRating(avgRating);
            report.setRemark(remark);
            report.setFeedbacks(feedbacks);
            report.setParticipants(participantRepository.findByEvent(event));
            reports.add(report);
        }

        return ResponseEntity.ok(reports);
    }

    /**
     * 🔹 Remove Participant from an Event
     */
    @DeleteMapping("/remove-participant/{participantId}")
    public ResponseEntity<String> removeParticipant(@PathVariable Long participantId) {
        Optional<Participant> participant = participantRepository.findById(participantId);
        if (participant.isEmpty()) {
            return ResponseEntity.badRequest().body("Participant not found.");
        }

        participantRepository.delete(participant.get());
        return ResponseEntity.ok("Participant removed from event.");
    }

    // 📄 DTO Class for sending full event reports
    public static class FullEventReport {
        private Long eventId;
        private String eventTitle;
        private String organizerName;
        private String organizerEmail;
        private double averageRating;
        private String remark;
        private List<Feedback> feedbacks;
        private List<Participant> participants;

        public Long getEventId() { return eventId; }
        public void setEventId(Long eventId) { this.eventId = eventId; }

        public String getEventTitle() { return eventTitle; }
        public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

        public String getOrganizerName() { return organizerName; }
        public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }

        public String getOrganizerEmail() { return organizerEmail; }
        public void setOrganizerEmail(String organizerEmail) { this.organizerEmail = organizerEmail; }

        public double getAverageRating() { return averageRating; }
        public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

        public String getRemark() { return remark; }
        public void setRemark(String remark) { this.remark = remark; }

        public List<Feedback> getFeedbacks() { return feedbacks; }
        public void setFeedbacks(List<Feedback> feedbacks) { this.feedbacks = feedbacks; }

        public List<Participant> getParticipants() { return participants; }
        public void setParticipants(List<Participant> participants) { this.participants = participants; }
    }
}
