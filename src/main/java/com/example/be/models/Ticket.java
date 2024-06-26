package com.example.be.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private ShowTime showTime;
    @ManyToOne
    @JsonBackReference
    private BookingHistory bookingHistory;
    @ManyToOne
    @JsonManagedReference("ticket-seat")
    private Seat seat;
    private int status;
}

