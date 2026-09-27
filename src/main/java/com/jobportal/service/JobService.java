package com.jobportal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jobportal.dto.JobRequest;
import com.jobportal.entity.Job;
import com.jobportal.entity.Recruiter;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.RecruiterRepository;
import com.jobportal.dto.JobUpdateRequest;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;

    public JobService(
            JobRepository jobRepository,
            RecruiterRepository recruiterRepository) {

        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
    }

    public Job createJob(JobRequest request, String email) {

        Recruiter recruiter =
                recruiterRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recruiter profile not found"));

        Job job = new Job();

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setSkills(request.getSkills());
        job.setJobType(request.getJobType());
        job.setCreatedAt(LocalDateTime.now());
        job.setRecruiter(recruiter);

        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    public List<Job> getRecruiterJobs(String email) {

        Recruiter recruiter =
                recruiterRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recruiter profile not found"));

        return jobRepository.findByRecruiterId(
                recruiter.getId());
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    public Page<Job> searchJobs(
            String keyword,
            Pageable pageable) {

        return jobRepository.searchJobs(
                keyword,
                pageable);
    }

    public Job updateJob(
            Long id,
            JobUpdateRequest request,
            String email) {

        Job existingJob =
                jobRepository.findById(id).orElse(null);

        if (existingJob == null) {
            return null;
        }

        Recruiter recruiter =
                recruiterRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recruiter profile not found"));

        if (!existingJob.getRecruiter().getId()
                .equals(recruiter.getId())) {

            throw new RuntimeException(
                    "You are not allowed to modify this job");
        }

        existingJob.setTitle(request.getTitle());
        existingJob.setDescription(request.getDescription());
        existingJob.setLocation(request.getLocation());
        existingJob.setSalary(request.getSalary());
        existingJob.setSkills(request.getSkills());
        existingJob.setJobType(request.getJobType());

        return jobRepository.save(existingJob);
    }

    public void deleteJob(
            Long id,
            String email) {

        Job job =
                jobRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"));

        Recruiter recruiter =
                recruiterRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recruiter profile not found"));

        if (!job.getRecruiter().getId()
                .equals(recruiter.getId())) {

            throw new RuntimeException(
                    "You are not allowed to delete this job");
        }

        jobRepository.delete(job);
    }

    public Page<Job> getJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }
}