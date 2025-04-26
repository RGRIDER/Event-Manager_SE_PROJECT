package com.example.demo.service;

import com.example.demo.models.Event;
import com.example.demo.models.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public Event createEvent(Event event, String organizerEmail) {
        Optional<User> organizer = userRepository.findByEmail(organizerEmail);
        if (organizer.isEmpty() || !"Organizer".equals(organizer.get().getUserType())) {
            throw new RuntimeException("Invalid organizer email.");
        }
        event.setOrganizer(organizer.get());
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> searchEvents(String title, String organizerName, String location) {
        return eventRepository.searchEvents(title, organizerName, location);
    }

    public List<Event> getEventsByOrganizer(User organizer) {
        return eventRepository.findByOrganizer(organizer);
    }

    public List<Event> getEventsByOrganizerEmail(String organizerEmail) {
        Optional<User> organizer = userRepository.findByEmail(organizerEmail);
        if (organizer.isPresent() && "Organizer".equals(organizer.get().getUserType())) {
            return eventRepository.findByOrganizer(organizer.get());
        } else {
            return List.of(); // return empty list if organizer not found instead of throwing
        }
    }

    public Optional<Event> getEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    public boolean deleteEvent(Long eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
            return true;
        }
        return false;
    }

    public Event updateEvent(Long eventId, Event updatedEvent) {
        Optional<Event> existingEventOpt = eventRepository.findById(eventId);
        if (existingEventOpt.isPresent()) {
            Event existingEvent = existingEventOpt.get();
            existingEvent.setTitle(updatedEvent.getTitle());
            existingEvent.setDescription(updatedEvent.getDescription());
            existingEvent.setDate(updatedEvent.getDate());
            existingEvent.setLocation(updatedEvent.getLocation());
            return eventRepository.save(existingEvent);
        } else {
            throw new RuntimeException("Event not found with ID: " + eventId);
        }
    }
}
