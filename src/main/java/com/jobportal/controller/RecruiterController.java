package com.jobportal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.dto.RecruiterRequest;
import com.jobportal.entity.Recruiter;
import com.jobportal.service.RecruiterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @PostMapping
    public ResponseEntity<Recruiter> createRecruiter(
            @Valid @RequestBody RecruiterRequest request,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                recruiterService.createRecruiter(
                        request,
                        user.getUsername()));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Recruiter> getRecruiterByUserId(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails user) {

        Recruiter recruiter =
                recruiterService.getRecruiterByUserId(userId);

        if (recruiter == null) {
            return ResponseEntity.notFound().build();
        }

        if (!recruiter.getUser().getEmail()
                .equals(user.getUsername())) {

            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(recruiter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recruiter> getRecruiter(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                recruiterService.getRecruiter(
                        id,
                        user.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recruiter> updateRecruiter(
            @PathVariable Long id,
            @Valid @RequestBody RecruiterRequest request,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                recruiterService.updateRecruiter(
                        id,
                        request,
                        user.getUsername()));
    }
}