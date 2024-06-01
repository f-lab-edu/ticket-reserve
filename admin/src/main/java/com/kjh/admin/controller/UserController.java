package com.kjh.admin.controller;

import com.kjh.admin.response.UserInfo;
import com.kjh.core.model.User;
import com.kjh.core.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my/info")
    public ResponseEntity<UserInfo> getMyInfo() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());
        return ResponseEntity.status(200).body(new UserInfo(user.getEmail()));
    }
}
