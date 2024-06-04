package com.kjh.core.service;

import com.kjh.core.exception.NotFoundException;
import com.kjh.core.jpa.UserRepository;
import com.kjh.core.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    public void createUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(encoder.encode(password));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundException.NOT_FOUND_USER);
    }

    @Transactional(readOnly = true)
    public Optional<User> getByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_USER);
    }
}
