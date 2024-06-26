package com.example.be.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableScreenReqDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
