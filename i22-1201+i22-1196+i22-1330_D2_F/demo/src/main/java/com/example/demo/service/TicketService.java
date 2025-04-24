package com.example.demo.service;

import com.example.demo.models.Ticket;
import com.example.demo.models.User;
import com.example.demo.models.Event;
import com.example.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket bookTicket(User user, Event event, double price, String status) {
        Ticket ticket = new Ticket(event, user, price, status);
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByUser(user);
    }

    public List<Ticket> getTicketsByEvent(Event event) {
        return ticketRepository.findByEvent(event);
    }

    public void cancelTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }
}

