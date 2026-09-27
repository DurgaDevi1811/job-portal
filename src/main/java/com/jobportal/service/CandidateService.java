package com.jobportal.service;

import org.springframework.stereotype.Service;

import com.jobportal.dto.CandidateRequest;
import com.jobportal.entity.Candidate;
import com.jobportal.entity.User;
import com.jobportal.repository.CandidateRepository;
import com.jobportal.repository.UserRepository;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;

    public CandidateService(
            CandidateRepository candidateRepository,
            UserRepository userRepository) {

        this.candidateRepository = candidateRepository;
        this.userRepository = userRepository;
    }

    public Candidate createCandidate(CandidateRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Candidate candidate = candidateRepository
                .findByUserId(user.getId())
                .orElse(new Candidate());

        candidate.setUser(user);
        candidate.setPhone(request.getPhone());
        candidate.setSkills(request.getSkills());
        candidate.setEducation(request.getEducation());
        candidate.setResume(request.getResume());

        return candidateRepository.save(candidate);
    }

    public Candidate getCandidate(Long id, String email) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Candidate not found"));

        if (!candidate.getUser().getEmail().equals(email)) {
            throw new RuntimeException(
                    "You are not allowed to access this profile");
        }

        return candidate;
    }
    public Candidate getCandidateByUserId(Long userId) {

        return candidateRepository.findByUserId(userId)
                .orElse(null);
    }

    public Candidate updateCandidate(
            Long id,
            CandidateRequest request,
            String email) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Candidate not found"));

        if (!candidate.getUser().getEmail().equals(email)) {
            throw new RuntimeException(
                    "You are not allowed to update this profile");
        }

        candidate.setPhone(request.getPhone());
        candidate.setSkills(request.getSkills());
        candidate.setEducation(request.getEducation());
        candidate.setResume(request.getResume());

        return candidateRepository.save(candidate);
    }
    public Candidate saveCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }
}