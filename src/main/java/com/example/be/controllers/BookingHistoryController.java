package com.example.be.controllers;

import com.example.be.models.BookingHistory;
import com.example.be.services.BookingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookingHistories")
public class BookingHistoryController {
    @Autowired
    private BookingHistoryService bookingHistoryService;

    @GetMapping("/{id}")
    public ResponseEntity<BookingHistory> get(@PathVariable int id) {
        return new ResponseEntity<>(bookingHistoryService.singleBookingHistory(id), HttpStatus.OK);
    }

    //  Booking feature
    @PostMapping
    ResponseEntity<BookingHistory> add(@RequestBody BookingHistory bookingHistory) {
        return new ResponseEntity<>(bookingHistoryService.add(bookingHistory), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<BookingHistory>> getByUserId(@RequestParam int userId) {
        return new ResponseEntity<>(bookingHistoryService.getBookingHistoriesByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/getLastBooking")
    public ResponseEntity<BookingHistory> getLastBooking(@RequestParam int userId) {
        return new ResponseEntity<>(bookingHistoryService.getLastBookingByUserId(userId), HttpStatus.OK);
    }
    @GetMapping("/revenue/{year}")
    public ResponseEntity<ResponseObject> getRevenueInYear(@PathVariable int year) {
        return new ResponseEntity<>(new ResponseObject("ok", "success", bookingHistoryService.getRevenueInYear(year)), HttpStatus.OK);
    }
    @GetMapping("/revenueMonthly/{year}")
    public ResponseEntity<ResponseObject> getRevenueMonthlyInYear(@PathVariable int year) {
        return new ResponseEntity<>(new ResponseObject("ok", "success", bookingHistoryService.getMonthlyRevenueInYear(year)), HttpStatus.OK);
    }
    @GetMapping("/allYears")
    public ResponseEntity<ResponseObject> getAllYears() {
        return new ResponseEntity<>(new ResponseObject("ok", "success", bookingHistoryService.getAllYearOfBookingHistories()), HttpStatus.OK);
    }
}
