package com.kjh.ticketreserve.controller;

import com.kjh.ticketreserve.Credentials;
import com.kjh.ticketreserve.jpa.UserRepository;
import com.kjh.ticketreserve.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class SigninController {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public SigninController(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signin(@RequestBody Credentials credentials) {
        Optional<User> maybeUser = repository.findByEmail(credentials.email());
        return maybeUser
            .filter(u -> encoder.matches(credentials.password(), u.getPasswordHash()))
            .map(u -> ResponseEntity.ok().build())
            .orElse(ResponseEntity.badRequest().build());
    }
}
