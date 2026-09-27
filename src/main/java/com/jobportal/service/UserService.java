package com.jobportal.service;

import com.jobportal.dto.LoginRequest;
import com.jobportal.dto.LoginResponse;
import com.jobportal.entity.User;
import com.jobportal.repository.UserRepository;
import com.jobportal.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(User user) {

        String email = user.getEmail().trim().toLowerCase();
        user.setEmail(email);

        if (user.getRole() == null ||
            (!user.getRole().equals("CANDIDATE")
            && !user.getRole().equals("RECRUITER"))) {

            throw new RuntimeException("Invalid account type");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request)
    {
        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()))
        {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole());
    }
    
}