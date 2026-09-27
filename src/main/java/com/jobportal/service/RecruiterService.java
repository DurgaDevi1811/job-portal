package com.jobportal.service;

import org.springframework.stereotype.Service;

import com.jobportal.dto.RecruiterRequest;
import com.jobportal.entity.Recruiter;
import com.jobportal.entity.User;
import com.jobportal.repository.RecruiterRepository;
import com.jobportal.repository.UserRepository;

@Service
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final UserRepository userRepository;

    public RecruiterService(
            RecruiterRepository recruiterRepository,
            UserRepository userRepository) {

        this.recruiterRepository = recruiterRepository;
        this.userRepository = userRepository;
    }

    public Recruiter createRecruiter(
            RecruiterRequest request,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Recruiter recruiter = recruiterRepository
                .findByUserId(user.getId())
                .orElse(new Recruiter());

        recruiter.setUser(user);
        recruiter.setCompany(request.getCompany());
        recruiter.setDesignation(request.getDesignation());

        return recruiterRepository.save(recruiter);
    }

    public Recruiter getRecruiterByUserId(Long userId) {

        return recruiterRepository.findByUserId(userId)
                .orElse(null);
    }
    public Recruiter getRecruiter(
            Long id,
            String email) {

        Recruiter recruiter = recruiterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Recruiter not found"));

        if (!recruiter.getUser().getEmail().equals(email)) {
            throw new RuntimeException(
                    "You are not allowed to access this profile");
        }

        return recruiter;
    }
    

    public Recruiter updateRecruiter(
            Long id,
            RecruiterRequest request,
            String email) {

        Recruiter recruiter = recruiterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Recruiter not found"));

        if (!recruiter.getUser().getEmail().equals(email)) {
            throw new RuntimeException(
                    "You are not allowed to update this profile");
        }

        recruiter.setCompany(request.getCompany());
        recruiter.setDesignation(request.getDesignation());

        return recruiterRepository.save(recruiter);
    }
}