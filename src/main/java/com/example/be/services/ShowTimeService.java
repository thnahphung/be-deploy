package com.example.be.services;

import com.example.be.dto.AddShowTimeReqDTO;
import com.example.be.dto.ShowTimeFilterDTO;
import com.example.be.models.*;
import com.example.be.repositories.ScreenShowTimeRepository;
import com.example.be.repositories.ShowTimeRepository;
import com.example.be.utils.ShowTimeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ShowTimeService {
    @Autowired
    private ShowTimeRepository showTimeRepository;

    @Autowired
    private ScreenService screenService;

    @Autowired
    private ScreenShowTimeRepository screenShowTimeRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private SeatService seatService;

    public ShowTime getById(int id) {
        return showTimeRepository.findById(id);
    }

    public List<ShowTime> getByMovieId(int id) {
        return showTimeRepository.findByMovieId(id);
    }

    public List<Date> getDatesAvailableByMovieId(int id) {
        return showTimeRepository.findDistinctStartDatesByMovieId(id);
    }

    public List<ShowTime> getAll() {
        return showTimeRepository.findAll();
    }

    public Page<ShowTime> filterShowTimes(ShowTimeFilterDTO filterDTO) {
        Specification<ShowTime> specification = Specification.where(null);

        if (filterDTO.getMovieTitle() != null && !filterDTO.getMovieTitle().isEmpty()) {
            specification = specification.and(ShowTimeSpecification.hasMovieTitle(filterDTO.getMovieTitle()));
        }
        if (filterDTO.getDate() != null) {
            specification = specification.and(ShowTimeSpecification.hasDate(filterDTO.getDate()));
        }

        Sort sort = Sort.by("startTime");
        if ("desc".equalsIgnoreCase(filterDTO.getSortDirection())) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);
        return showTimeRepository.findAll(specification, pageable);
    }

    public Page<ShowTime> filterShowTimesByMonth(ShowTimeFilterDTO filterDTO) {
        Specification<ShowTime> specification = Specification.where(null);

        if (filterDTO.getMovieTitle() != null && !filterDTO.getMovieTitle().isEmpty()) {
            specification = specification.and(ShowTimeSpecification.hasMovieTitle(filterDTO.getMovieTitle()));
        }
        if (filterDTO.getDate() != null) {
            specification = specification.and(ShowTimeSpecification.hasMonth(filterDTO.getDate()));
        }

        Sort sort = Sort.by("startTime");
        if ("desc".equalsIgnoreCase(filterDTO.getSortDirection())) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);
        return showTimeRepository.findAll(specification, pageable);
    }

    public Page<ShowTime> filterShowTimesByYear(ShowTimeFilterDTO filterDTO) {
        Specification<ShowTime> specification = Specification.where(null);

        if (filterDTO.getMovieTitle() != null && !filterDTO.getMovieTitle().isEmpty()) {
            specification = specification.and(ShowTimeSpecification.hasMovieTitle(filterDTO.getMovieTitle()));
        }
        if (filterDTO.getDate().getYear() > 0) {
            specification = specification.and(ShowTimeSpecification.hasYear(filterDTO.getDate().getYear()));
        }

        Sort sort = Sort.by("startTime");
        if ("desc".equalsIgnoreCase(filterDTO.getSortDirection())) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);
        return showTimeRepository.findAll(specification, pageable);
    }

    public ShowTime add(AddShowTimeReqDTO addShowTimeReqDTO) {
        Screen screen = screenService.get(addShowTimeReqDTO.getScreenId());
        Movie movie = movieService.get(addShowTimeReqDTO.getMovieId());
        if (screen == null || movie == null) {
            return null;
        }
        ScreenShowTime screenShowTime = new ScreenShowTime();
        screenShowTime.setScreen(screen);
        screenShowTime.setCreateTime(LocalDateTime.now());
        screenShowTime.setStatus("1");
        screenShowTime = screenShowTimeRepository.save(screenShowTime);

        ShowTime showTime = new ShowTime();
        showTime.setScreenShowTime(screenShowTime);
        showTime.setMovie(movie);
        showTime.setStartTime(addShowTimeReqDTO.getStartTime());
        showTime.setEndTime(addShowTimeReqDTO.getEndTime());
        showTime.setPrice(addShowTimeReqDTO.getPrice());
        showTime.setStatus(1);
        showTime = showTimeRepository.save(showTime);

        seatService.addSeats(screenShowTime);

        return showTime;
    }

}
