package com.example.be.services;

import com.example.be.models.BookingHistory;
import com.example.be.models.Ticket;
import com.example.be.repositories.BookingHistoryRepository;
import com.example.be.repositories.SeatRepository;
import com.example.be.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BookingHistoryService {
    @Autowired
    private BookingHistoryRepository bookingHistoryRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SeatRepository seatRepository;

    public BookingHistory singleBookingHistory(int id) {
        return bookingHistoryRepository.findById(id);
    }

    //  Booking feature
    public BookingHistory add(BookingHistory bookingHistory) {
        return bookingHistoryRepository.save(bookingHistory);
    }

    private BookingHistory clearBookingHistory(BookingHistory bookingHistory) {
        bookingHistory.getUser().setBookingHistories(null);
        for (Ticket ticket : bookingHistory.getTickets()) {
            ticket.setShowTime(null);
            ticket.setBookingHistory(null);
//            ticket.getSeat().setScreen(null);
            ticket.getSeat().setTickets(null);
        }
        return bookingHistory;
    }

    private List<BookingHistory> clearBookingHistories(List<BookingHistory> bookingHistoryList) {
        for (BookingHistory bookingHistory : bookingHistoryList) {
            clearBookingHistory(bookingHistory);
        }
        return bookingHistoryList;
    }

    public List<BookingHistory> getBookingHistoriesByUserId(int id) {
        return  bookingHistoryRepository.findByUserId(id);
    }

    public BookingHistory getLastBookingByUserId(int userId) {
        return bookingHistoryRepository.findTop1ByUserIdOrderByTimeDesc(userId);
    }
    public int getRevenueInYear(int year) {
        return bookingHistoryRepository.findTotalByYearAndStatus(year, BookingHistory.SUCCESS);
    }

    //    monthly revenue in year
    public int[] getMonthlyRevenueInYear(int year) {
        List<Object[]> results = bookingHistoryRepository.findMonthlyTotalByYearAndStatus(year, BookingHistory.SUCCESS);
        int[] monthlyTotals = new int[12];
        Arrays.fill(monthlyTotals, 0);
        for (Object[] result : results) {
            if (result.length == 2) {
                Integer month = (Integer) result[0];
                System.out.println(month);
                Long total = (Long) result[1];
                System.out.println(total);
                if (month != null && total != null) {
                    monthlyTotals[month - 1] = total.intValue();
                }
            }
        }

        return monthlyTotals;
    }
    public List<Integer> getAllYearOfBookingHistories(){
        return bookingHistoryRepository.findAllYears(BookingHistory.SUCCESS);
    }
}
