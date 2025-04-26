package com.example.demo.repository;

import com.example.demo.models.Event;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByOrganizer(User organizer);

    @Query("SELECT e FROM Event e " +
            "WHERE (:title IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:organizerName IS NULL OR LOWER(CONCAT(e.organizer.firstName, ' ', e.organizer.lastName)) LIKE LOWER(CONCAT('%', :organizerName, '%'))) " +
            "AND (:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<Event> searchEvents(
            @Param("title") String title,
            @Param("organizerName") String organizerName,
            @Param("location") String location
    );
}
