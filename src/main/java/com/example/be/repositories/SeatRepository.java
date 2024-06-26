package com.example.be.repositories;

import com.example.be.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
    Seat findByIdAndStatus(int id, int status);

    Seat findById(int id);

    List<Seat> findByScreenShowTimeId(int id);
}
