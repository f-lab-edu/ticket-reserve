package com.kjh.ticketreserve.controller;

import com.kjh.ticketreserve.AccessTokenCarrier;
import com.kjh.ticketreserve.Credentials;
import com.kjh.ticketreserve.exception.BadRequestException;
import com.kjh.ticketreserve.model.User;
import com.kjh.ticketreserve.security.JwtProvider;
import com.kjh.ticketreserve.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class SigninController {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    public SigninController(UserService userService, PasswordEncoder encoder, JwtProvider jwtProvider) {
        this.userService = userService;
        this.encoder = encoder;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<AccessTokenCarrier> signin(@RequestBody Credentials credentials) {
        Optional<User> maybeUser = userService.getByEmailOptional(credentials.email());
        return maybeUser
            .filter(u -> encoder.matches(credentials.password(), u.getPasswordHash()))
            .map(u -> jwtProvider.createToken(u.getEmail()))
            .map(token -> ResponseEntity.ok(new AccessTokenCarrier(token)))
            .orElseThrow(BadRequestException.BAD_CREDENTIALS);
    }
}
