package com.example.be.controllers;

import com.example.be.dto.AddShowTimeReqDTO;
import com.example.be.dto.AvailableScreenReqDTO;
import com.example.be.dto.ShowTimeFilterDTO;
import com.example.be.models.ShowTime;
import com.example.be.services.ScreenService;
import com.example.be.services.ShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
public class ShowTimeController {
    @Autowired
    private ShowTimeService showTimeService;

    @Autowired
    private ScreenService screenService;

    @GetMapping("/{id}")
    public ResponseEntity<ShowTime> getById(@PathVariable int id) {
        return new ResponseEntity<>(showTimeService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/dateByMovieId/{id}")
    public ResponseEntity<List<Date>> getShowTimesMovie(@PathVariable int id) {
        return new ResponseEntity<>(showTimeService.getDatesAvailableByMovieId(id), HttpStatus.OK);
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<List<ShowTime>> getShowTimesByMovieId(@PathVariable int id) {
        return new ResponseEntity<>(showTimeService.getByMovieId(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ShowTime>> getAll() {
        return new ResponseEntity<>(showTimeService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<ResponseObject> filterShowTimes(@RequestBody ShowTimeFilterDTO filterDTO) {
        Page<ShowTime> showTimes = null;
        if (filterDTO.getBy() == 1) {
            showTimes = showTimeService.filterShowTimes(filterDTO);
        } else if (filterDTO.getBy() == 2) {
            showTimes = showTimeService.filterShowTimesByMonth(filterDTO);
        }else{
            showTimes = showTimeService.filterShowTimesByYear(filterDTO);
        }
        return ResponseEntity.ok(new ResponseObject("ok", "success", showTimes));
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addShowTime(@RequestBody AddShowTimeReqDTO addShowTimeReqDTO) {
        return new ResponseEntity<>(new ResponseObject("ok", "success", showTimeService.add(addShowTimeReqDTO)), HttpStatus.OK);
    }

    @PostMapping("/screenAvailable")
    public ResponseEntity<ResponseObject> getScreenAvailable(@RequestBody AvailableScreenReqDTO availableScreenReqDTO) {
        return new ResponseEntity<>(new ResponseObject("ok", "success", screenService.getAvailableScreens(availableScreenReqDTO.getStartTime(), availableScreenReqDTO.getEndTime())), HttpStatus.OK);
    }

}
