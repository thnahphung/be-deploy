package com.example.be.repositories;

import com.example.be.models.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreenRepository extends JpaRepository<Screen, Integer> {
    List<Screen> findAll();

    Screen findById(int id);

    @Query("SELECT s FROM Screen s WHERE s.id NOT IN (" +
            "SELECT st.screenShowTime.screen.id FROM ShowTime st WHERE " +
            "(:startTime BETWEEN st.startTime AND st.endTime OR :endTime BETWEEN st.startTime AND st.endTime OR " +
            "st.startTime BETWEEN :startTime AND :endTime))")
    List<Screen> findAvailableScreens(@Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);
}
