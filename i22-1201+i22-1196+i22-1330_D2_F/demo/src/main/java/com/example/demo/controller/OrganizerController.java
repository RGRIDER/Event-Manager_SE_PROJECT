package com.example.demo.controller;

import com.example.demo.models.Event;
import com.example.demo.models.Feedback;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.FeedbackRepository;
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
    private EventRepository eventRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @GetMapping("/feedback-report/{eventId}")
    public ResponseEntity<FeedbackReport> getFeedbackReport(@PathVariable Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Feedback> feedbacks = feedbackRepository.findByParticipant_Event_EventId(eventId);
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

    public static class FeedbackReport {
        private double averageRating;
        private String remark;
        private List<Feedback> feedbacks;

        public double getAverageRating() { return averageRating; }
        public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

        public String getRemark() { return remark; }
        public void setRemark(String remark) { this.remark = remark; }

        public List<Feedback> getFeedbacks() { return feedbacks; }
        public void setFeedbacks(List<Feedback> feedbacks) { this.feedbacks = feedbacks; }
    }
}
