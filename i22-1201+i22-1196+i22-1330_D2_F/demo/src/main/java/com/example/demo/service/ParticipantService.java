package com.example.demo.service;

import com.example.demo.models.Participant;
import com.example.demo.models.User;
import com.example.demo.models.Event;
import com.example.demo.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

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


    // NEW METHOD: Unregister user from event by email and eventId
    public boolean unregisterFromEvent(User user, Event event) {
        Optional<Participant> participant = participantRepository.findByUserAndEvent(user, event);
        if (participant.isPresent()) {
            participantRepository.delete(participant.get());
            return true;
        }
        return false; // User is not registered for the event
    }
}
