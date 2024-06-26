package com.example.be.services;

import com.example.be.dto.RegisterReqDTO;
import com.example.be.dto.UserDTO;
import com.example.be.models.BookingHistory;
import com.example.be.models.Ticket;
import com.example.be.models.User;
import com.example.be.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public List<User> all() {
        return userRepository.findAll();
    }

    public User get(int id) {
        User user = userRepository.findById(id);
//        clear(user);
        user.setBookingHistories(null);
        return user;
    }

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    public User login(String email, String password) {
        User foundUser = userRepository.findByEmail(email);
        return foundUser != null && passwordEncoder.matches(password, foundUser.getPassword()) ? foundUser : null;
    }

    public User update(UserDTO userDTO) {
        User u = userRepository.findById(userDTO.getId());
        if (u == null) {
            return null;
        }
        u.setName(userDTO.getName());
        u.setEmail(userDTO.getEmail());
        u.setPhone(userDTO.getPhone());
        u.setBirth(userDTO.getBirth());
        return userRepository.save(u);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void clear(User user) {
        for (BookingHistory bookingHistory : user.getBookingHistories()) {
            bookingHistory.setUser(null);
            for (Ticket ticket : bookingHistory.getTickets()) {
                ticket.setShowTime(null);
                ticket.setBookingHistory(null);
                ticket.getSeat().setTickets(null);
//                ticket.getSeat().setScreen(null);
            }
        }
    }

    public boolean isExistEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public User register(RegisterReqDTO registerReqDTO) {
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(registerReqDTO.getPassword()));
        newUser.setEmail(registerReqDTO.getEmail());
        newUser.setName(registerReqDTO.getName());
        newUser.setPhone(registerReqDTO.getPhone());
        newUser.setBirth(registerReqDTO.getBirth());
        newUser.setRole(0);
        newUser.setStatus(1);
        return userRepository.save(newUser);
    }

}
