package com.jobportal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.dto.LoginRequest;
import com.jobportal.dto.LoginResponse;
import com.jobportal.dto.RegisterRequest;
import com.jobportal.entity.User;
import com.jobportal.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final UserService userService;

    public AuthController(UserService userService)
    {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request)
    {
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        userService.register(user);

        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(
                userService.login(request));
    }
}