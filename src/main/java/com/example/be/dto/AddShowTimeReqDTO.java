package com.example.be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddShowTimeReqDTO {
    private int movieId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int price;
    private int screenId;
}
