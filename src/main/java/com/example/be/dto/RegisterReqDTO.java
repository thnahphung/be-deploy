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
public class RegisterReqDTO {
    private String email;
    private String password;
    private String name;
    private String phone;
    private LocalDateTime birth;
    private String verificationCode;
}
