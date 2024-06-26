package com.kjh.admin.controller;

import com.kjh.admin.request.SignupRequest;
import com.kjh.core.exception.BadRequestException;
import com.kjh.core.service.UserService;
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
