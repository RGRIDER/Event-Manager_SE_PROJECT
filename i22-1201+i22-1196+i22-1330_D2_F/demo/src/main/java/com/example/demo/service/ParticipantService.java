package com.example.demo.service;

import com.example.demo.models.Participant;
import com.example.demo.models.User;
import com.example.demo.models.Event;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    public boolean registerForEvent(User user, Event event) {
        if (participantRepository.findByUserAndEvent(user, event).isPresent()) {
            return false; // User is already registered
        }
        participantRepository.save(new Participant(user, event));
        return true;
    }

    public List<Participant> getParticipantsByEvent(Event event) {
        return participantRepository.findByEvent(event);
    }

    public List<Participant> getEventsByUser(User user) {
        return participantRepository.findByUser(user);
    }

    public List<Participant> getEventsByUser(String email) {
        return participantRepository.findByUser_Email(email);
    }

    public boolean unregisterFromEvent(User user, Event event) {
        Optional<Participant> participantOpt = participantRepository.findByUserAndEvent(user, event);
        if (participantOpt.isPresent()) {
            Participant participant = participantOpt.get();
            // First delete all feedbacks associated with this participant
            feedbackRepository.deleteAll(feedbackRepository.findByParticipant_Event_EventId(event.getEventId()));
            // Then delete the participant
            participantRepository.delete(participant);
            return true;
        }
        return false; // User is not registered for the event
    }

    public Optional<Participant> findByUserEmailAndEventId(String email, Long eventId) {
        return participantRepository.findByUser_EmailAndEvent_EventId(email, eventId);
    }
}
