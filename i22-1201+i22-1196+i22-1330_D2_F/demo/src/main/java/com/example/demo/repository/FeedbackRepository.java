package com.example.demo.repository;

import com.example.demo.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByParticipant_Event_EventId(Long eventId);
}
