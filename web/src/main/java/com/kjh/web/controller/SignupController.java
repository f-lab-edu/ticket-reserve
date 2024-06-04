package com.kjh.web.controller;

import com.kjh.core.exception.BadRequestException;
import com.kjh.core.service.UserService;
import com.kjh.web.request.SignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
