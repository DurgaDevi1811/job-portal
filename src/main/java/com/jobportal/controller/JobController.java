package com.jobportal.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.dto.JobRequest;
import com.jobportal.dto.JobUpdateRequest;
import com.jobportal.entity.Job;
import com.jobportal.service.JobService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<Job> createJob(
            @Valid @RequestBody JobRequest request,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                jobService.createJob(
                        request,
                        user.getUsername()));
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/recruiter")
    public List<Job> getRecruiterJobs(
            @AuthenticationPrincipal UserDetails user) {

        return jobService.getRecruiterJobs(
                user.getUsername());
    }

    @GetMapping("/search")
    public Page<Job> search(
            @RequestParam String keyword,
            Pageable pageable) {

        return jobService.searchJobs(
                keyword,
                pageable);
    }

    @GetMapping("/page")
    public Page<Job> getJobs(
            Pageable pageable) {

        return jobService.getJobs(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(
            @PathVariable Long id) {

        Job job = jobService.getJobById(id);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(job);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobUpdateRequest request,
            @AuthenticationPrincipal UserDetails user) {

        Job updated = jobService.updateJob(
                id,
                request,
                user.getUsername());

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {

        jobService.deleteJob(
                id,
                user.getUsername());

        return ResponseEntity.noContent().build();
    }
}