package com.example.be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResDTO {
    private String status;
    private String message;
    private String url;
}
