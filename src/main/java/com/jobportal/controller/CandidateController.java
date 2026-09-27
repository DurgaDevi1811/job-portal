package com.jobportal.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.jobportal.dto.CandidateRequest;
import com.jobportal.entity.Candidate;
import com.jobportal.service.CandidateService;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public ResponseEntity<Candidate> createCandidate(
            @RequestBody CandidateRequest request,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                candidateService.createCandidate(
                        request,
                        user.getUsername()));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Candidate> getCandidateByUserId(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails user) {

        Candidate candidate =
                candidateService.getCandidateByUserId(userId);

        if (candidate == null) {
            return ResponseEntity.notFound().build();
        }

        if (!candidate.getUser().getEmail()
                .equals(user.getUsername())) {

            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(candidate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidate(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                candidateService.getCandidate(
                        id,
                        user.getUsername()));
    }
    
    
    
    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateCandidate(
            @PathVariable Long id,
            @RequestBody CandidateRequest request,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                candidateService.updateCandidate(
                        id,
                        request,
                        user.getUsername()));
    }
    
    @GetMapping("/{id}/resume")
    public ResponseEntity<?> viewResume(
            @PathVariable Long id,
            Authentication authentication) {

        try {
            Candidate candidate = candidateService.getCandidate(
                    id,
                    authentication.getName()
            );

            if (candidate.getResume() == null ||
                    candidate.getResume().isBlank()) {

                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get("uploads/resumes")
                    .resolve(candidate.getResume())
                    .normalize();

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(
                    filePath.toUri()
            );

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" +
                                    candidate.getResume() +
                                    "\""
                    )
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Unable to open resume.");
        }
    }
    
    @PostMapping("/{id}/resume")
    public ResponseEntity<?> uploadResume(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        try {
            Candidate candidate = candidateService.getCandidate(
                    id,
                    authentication.getName()
            );

            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Please select a resume file.");
            }

            String fileName = file.getOriginalFilename();

            if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
                return ResponseEntity.badRequest()
                        .body("Only PDF files are allowed.");
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest()
                        .body("Resume size must be less than 5 MB.");
            }

            Path uploadDirectory = Paths.get("uploads/resumes");

            Files.createDirectories(uploadDirectory);

            String uniqueFileName =
                    candidate.getId() + "_" + System.currentTimeMillis() + ".pdf";

            Path filePath = uploadDirectory.resolve(uniqueFileName);

            Files.write(filePath, file.getBytes());

            candidate.setResume(uniqueFileName);

            Candidate savedCandidate = candidateService.saveCandidate(candidate);

            return ResponseEntity.ok(savedCandidate);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body("Unable to open resume: " + e.getMessage());
        }
        
    }
    @GetMapping("/{id}/resume/download")
    public ResponseEntity<?> downloadResume(
            @PathVariable Long id,
            Authentication authentication) {

        try {
            Candidate candidate =
                    candidateService.getCandidate(id, authentication.getName());

            if (candidate.getResume() == null ||
                    candidate.getResume().isBlank()) {

                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get("uploads/resumes")
                    .resolve(candidate.getResume())
                    .normalize();

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource =
                    new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                                    candidate.getResume() + "\""
                    )
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body("Unable to download resume: ");
        }
    }
}