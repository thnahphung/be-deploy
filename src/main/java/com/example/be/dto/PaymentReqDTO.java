package com.example.be.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentReqDTO {
    List<Integer> seatIds;
    int userId;
    int discount;
    int showTimeId;

}
