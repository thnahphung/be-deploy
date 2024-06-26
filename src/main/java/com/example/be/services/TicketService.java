package com.example.be.services;

import com.example.be.models.BookingHistory;
import com.example.be.models.Seat;
import com.example.be.models.ShowTime;
import com.example.be.models.Ticket;
import com.example.be.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public Ticket get(int id) {
        return clearTicket(ticketRepository.findById(id));
    }

    public Ticket add(Ticket ticket) {
        Ticket ticketSaved = ticketRepository.save(ticket);
        return clearTicket(ticketSaved);
    }


    public List<Ticket> getListsTickets(List<Integer> ticketIds) {
        return ticketRepository.findAllById(ticketIds);
    }

    public List<Ticket> addListTickets(List<Seat> seats, ShowTime showTime, BookingHistory bookingHistory) {
        List<Ticket> tickets = new ArrayList<>();
        for (Seat seat : seats) {
            Ticket ticket = new Ticket();
            ticket.setSeat(seat);
            ticket.setBookingHistory(bookingHistory);
            ticket.setShowTime(showTime);
            ticket.setStatus(1);
            tickets.add(ticket);
        }
        return clearTickets(ticketRepository.saveAll(tickets));
    }

    public List<Ticket> clearTickets(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            clearTicket(ticket);
        }
        return tickets;
    }

    public Ticket clearTicket(Ticket ticket) {
        ticket.getShowTime().getMovie().setShowTimes(null);
        ticket.getSeat().setTickets(null);
        ticket.getShowTime().setTickets(null);
        return ticket;
    }

}
