package com.example.demo.repository;

import com.example.demo.models.Ticket;
import com.example.demo.models.User;
import com.example.demo.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUser(User user);
    List<Ticket> findByEvent(Event event);
}

