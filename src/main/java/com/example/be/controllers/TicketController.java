package com.example.be.controllers;

import com.example.be.models.Ticket;
import com.example.be.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;


    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        return new ResponseEntity<>(ticketService.add(ticket), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> get(@PathVariable int id) {
        return new ResponseEntity<>(ticketService.get(id), HttpStatus.OK);
    }
}
