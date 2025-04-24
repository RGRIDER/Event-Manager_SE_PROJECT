package com.example.demo.service;

import com.example.demo.models.Event;
import com.example.demo.models.User;
import com.example.demo.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    /**
     * Create a new event
     */
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    /**
     * Get all events created by a specific organizer
     */
    public List<Event> getEventsByOrganizer(User organizer) {
        return eventRepository.findByOrganizer(organizer);
    }

    /**
     * Get event by ID
     */
    public Optional<Event> getEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    /**
     * Get all events (for admin purposes)
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Delete an event by ID
     */
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    /**
     * Update an existing event
     */
    public Event updateEvent(Event updatedEvent) {
        return eventRepository.save(updatedEvent);
    }

    /**
     * Check if an event with the given title already exists
     */
    public boolean existsByTitle(String title) {
        return eventRepository.existsByTitle(title);
    }
}