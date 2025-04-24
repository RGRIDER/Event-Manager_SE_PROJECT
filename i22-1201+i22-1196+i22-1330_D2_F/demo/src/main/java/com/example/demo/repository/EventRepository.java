package com.example.demo.repository;

import com.example.demo.models.Event;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    /**
     * Find all events created by a specific organizer
     */
    List<Event> findByOrganizer(User organizer);

    /**
     * Check if an event with the given title already exists
     */
    boolean existsByTitle(String title);
}