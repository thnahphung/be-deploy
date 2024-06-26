package com.example.be.repositories;

import com.example.be.models.ScreenShowTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenShowTimeRepository extends JpaRepository<ScreenShowTime, Integer> {
    ScreenShowTime findById(int id);
}
