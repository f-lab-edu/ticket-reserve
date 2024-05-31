package com.kjh.ticketreserve.controller;

import com.kjh.ticketreserve.SignupRequest;
import com.kjh.ticketreserve.exception.BadRequestException;
import com.kjh.ticketreserve.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody SignupRequest signupRequest) {
        try {
            userService.createUser(signupRequest.email(), signupRequest.password());
        } catch (Exception e) {
            throw BadRequestException.DUPLICATED_EMAIL.get();
        }
        return ResponseEntity.ok().build();
    }
}
