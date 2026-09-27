package com.jobportal.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import jakarta.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.jobportal.dto.ApplicationRequest;
import com.jobportal.entity.Application;
import com.jobportal.entity.Candidate;
import com.jobportal.service.ApplicationService;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<Application> apply(
            @Valid @RequestBody ApplicationRequest request,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                applicationService.apply(
                        request,
                        user.getUsername()));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Application>> myApplications(
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                applicationService.getCandidateApplications(
                        user.getUsername()));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> jobApplications(
            @PathVariable Long jobId,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                applicationService.getJobApplications(
                        jobId,
                        user.getUsername()));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Application> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                applicationService.updateStatus(
                        id,
                        status,
                        user.getUsername()));
    }
    @GetMapping("/{id}/resume")
    public ResponseEntity<?> viewApplicantResume(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {

        try {

            Application application =
                    applicationService.getApplicationForRecruiter(
                            id,
                            user.getUsername());

            Candidate candidate = application.getCandidate();

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
                            "inline; filename=\"" +
                                    candidate.getResume() + "\"")
                    .body(resource);

        } catch (Exception e) {


            return ResponseEntity.internalServerError()
                    .body("Unable to view resume.");
        }
    }
    @GetMapping("/{id}/resume/download")
    public ResponseEntity<?> downloadApplicantResume(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {

        try {

            Application application =
                    applicationService.getApplicationForRecruiter(
                            id,
                            user.getUsername());

            Candidate candidate = application.getCandidate();

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
                                    candidate.getResume() + "\"")
                    .body(resource);

        } catch (Exception e) {


            return ResponseEntity.internalServerError()
                    .body("Unable to download resume:");
        }
    }
}