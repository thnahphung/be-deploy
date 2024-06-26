package com.example.be.controllers;

import com.example.be.dto.RegisterReqDTO;
import com.example.be.dto.ReqLoginDTO;
import com.example.be.dto.UserDTO;
import com.example.be.models.User;
import com.example.be.models.VerificationCode;
import com.example.be.services.EmailService;
import com.example.be.services.UserService;
import com.example.be.services.VerificationCodeService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationCodeService verificationCodeService;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.all(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return new ResponseEntity<>(userService.get(id), HttpStatus.OK);
    }

    @PostMapping("/login")
    ResponseEntity<ResponseObject> login(@RequestBody ReqLoginDTO reqLoginDTO) {
        User userFind = userService.login(reqLoginDTO.getEmail(), reqLoginDTO.getPassword());
        return userFind != null ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Login successful", userFind)
                ) :
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("failed", "Invalid username or password", "")
                );
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> update(@RequestBody UserDTO userDTO) {
        User user = userService.update(userDTO);
        return user == null ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("failed", "Update fail", "")
        ) : ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update successful", user)
        );
    }

    ResponseEntity<ResponseObject> sendBillEmail(@RequestParam String email) {
        try {
            User user = userService.findByEmail(email);
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("name", user.getName());
            emailService.sendHtmlEmailPaymentSuccess(email, "Your Bill!", templateModel);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Send new password successful", "")
            );
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", "Send new password fail", "")
            );
        }
    }

    @PostMapping("/register")
    ResponseEntity<ResponseObject> register(@RequestBody RegisterReqDTO registerReqDTO) {
        if (!verificationCodeService.isExist(registerReqDTO.getEmail(), registerReqDTO.getVerificationCode())) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", "Invalid verification code", "")
            );
        }
        if (userService.isExistEmail(registerReqDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", "Email already exists", "")
            );
        }
        User newUser = userService.register(registerReqDTO);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Register successful", newUser)
        );
    }
    @GetMapping("/sendVerificationEmail")
    ResponseEntity<ResponseObject> sendVerificationEmail(@RequestParam String email, @RequestParam String name) {
        try {
            String code = userService.generateVerificationCode();
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setEmail(email);
            verificationCode.setCode(code);
            verificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(5));
            verificationCodeService.save(verificationCode);

            Map<String, Object> templateModel = new HashMap<>();

            templateModel.put("name", name);
            templateModel.put("verificationCode", code);

            emailService.sendHtmlEmailVerificationCode(email, "Your Verification Code", templateModel);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Send Verification Code successful", "")
            );
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", "Send Verification Code fail", "")
            );
        }
    }

}
