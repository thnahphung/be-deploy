package com.example.be.repositories;

import com.example.be.models.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingHistoryRepository extends JpaRepository<BookingHistory,Integer> {
    BookingHistory findById(int id);
    List<BookingHistory> findByUserId(int userId);
    BookingHistory findTop1ByUserIdOrderByTimeDesc(int userId);
        @Query("SELECT SUM(bh.total) FROM BookingHistory bh WHERE YEAR(bh.time) = :year AND bh.status = :status")
    int findTotalByYearAndStatus(@Param("year") int year, @Param("status") int status);

    @Query("SELECT bh FROM BookingHistory bh WHERE YEAR(bh.time) = :year AND bh.status = :status")
    List<BookingHistory> findAllByYearAndStatus(@Param("year") int year, @Param("status") int status);

    @Query("SELECT MONTH(bh.time) as month, SUM(bh.total) as sum FROM BookingHistory bh WHERE YEAR(bh.time) = :year AND bh.status = :status GROUP BY MONTH(bh.time)")
    List<Object[]> findMonthlyTotalByYearAndStatus(@Param("year") int year, @Param("status") int status);
    @Query("SELECT YEAR(bh.time) FROM BookingHistory bh WHERE bh.status = :status GROUP BY YEAR(bh.time)")
    List<Integer> findAllYears(@Param("status") int status);
}
