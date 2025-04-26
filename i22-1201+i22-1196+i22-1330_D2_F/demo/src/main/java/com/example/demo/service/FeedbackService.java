package com.example.demo.service;

import com.example.demo.models.Feedback;
import com.example.demo.models.Participant;
import com.example.demo.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback saveFeedback(Participant participant, String content, int rating) {
        Feedback feedback = new Feedback(participant, content, rating);
        return feedbackRepository.save(feedback);
    }
}
