package com.example.be.services;

import com.example.be.models.Screen;
import com.example.be.repositories.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    public Screen get(int id) {
        return screenRepository.findById(id);
    }

    public List<Screen> getAvailableScreens(LocalDateTime startTime, LocalDateTime endTime) {
        return screenRepository.findAvailableScreens(startTime, endTime);
    }

}
