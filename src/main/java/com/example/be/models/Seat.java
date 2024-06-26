package com.example.be.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "seats")
public class Seat {
    public static int BOOKED = 0;
    public static int AVAILABLE = 1;
    public static int SELECTED = 2;
    public static int UNAVAILABLE = 3;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String seatIndex;
    private int status;
    @ManyToOne
    @JsonBackReference
    private ScreenShowTime screenShowTime;
    @OneToMany(mappedBy = "seat")
    @JsonBackReference("ticket-seat")
    private List<Ticket> tickets;

}


