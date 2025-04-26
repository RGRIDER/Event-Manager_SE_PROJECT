package com.example.demo.repository;

import com.example.demo.models.Participant;
import com.example.demo.models.User;
import com.example.demo.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByEvent(Event event);
    List<Participant> findByUser(User user);
    List<Participant> findByUser_Email(String email);
    Optional<Participant> findByUserAndEvent(User user, Event event);

    Optional<Participant> findByUser_EmailAndEvent_EventId(String email, Long eventId);
}
