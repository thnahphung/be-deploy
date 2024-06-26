package com.example.be.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MovieDTO {
    private int id;
    private String nameVn;
    private String director;
    private String actor;
    private String country_name_vn;
    private String type_name_vn;
    private String release_date;
    private String end_date;
    private String brief_vn;
    private String image;
    private String trailer;
    private String ratings;
    private String time;
    private String limitage_vn;
    private int sort_order;
}
