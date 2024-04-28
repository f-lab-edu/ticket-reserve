package com.kjh.ticketreserve.controller;

import com.kjh.ticketreserve.UserInfo;
import com.kjh.ticketreserve.exception.BadRequestException;
import com.kjh.ticketreserve.jpa.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/my/info")
    public UserInfo getMyInfo() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
            .map(u -> new UserInfo(u.getEmail()))
            .orElseThrow(BadRequestException.NOT_FOUND::get);
    }
}
