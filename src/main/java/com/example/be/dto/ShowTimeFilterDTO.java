package com.example.be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimeFilterDTO {
    private String movieTitle;
    private LocalDate date;
    private String sortDirection;
    private int by;
    private int page;
    private int size;
}
