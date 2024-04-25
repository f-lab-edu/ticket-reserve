package com.kjh.ticketreserve.controller;

import com.kjh.ticketreserve.exception.BadRequestException;
import com.kjh.ticketreserve.jpa.UserRepository;
import com.kjh.ticketreserve.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SignupController {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public SignupController(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody Map<String, String> map) {
        User user = new User();
        user.setEmail(map.get("email"));
        user.setPasswordHash(encoder.encode(map.get("password")));
        try {
            repository.save(user);
        } catch (Exception e) {
            throw BadRequestException.DUPLICATED_EMAIL.get();
        }
        return ResponseEntity.ok().build();
    }
}
