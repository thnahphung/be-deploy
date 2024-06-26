package com.example.be.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "booking_histories")
public class BookingHistory {
    public static final int PENDING = 0;
    public static final int SUCCESS = 1;
    public static final int FAILED = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "bookingHistory")
    private List<Ticket> tickets;
    private int discount;
    private int total;
    private LocalDateTime time;
    private int status;
}
