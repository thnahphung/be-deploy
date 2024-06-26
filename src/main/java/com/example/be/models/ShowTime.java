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
@Table(name = "show_times")
public class ShowTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "movie_id",
            foreignKey = @ForeignKey(name = "fk_show_times_movies"))
    private Movie movie;
    @OneToOne
    private ScreenShowTime screenShowTime;
    @OneToMany(mappedBy = "showTime")
    @JsonBackReference
    private List<Ticket> tickets;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int price;
    private int status;
}
