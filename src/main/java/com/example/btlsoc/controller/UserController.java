package com.example.btlsoc.controller;

import com.example.btlsoc.model.User;
import com.example.btlsoc.repository.UserRepository;
import com.example.btlsoc.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public Optional<User> getCurrentUser(@CurrentUser User user) {
        return userRepository.findById(user.getId());
    }
}
