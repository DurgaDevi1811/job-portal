package com.jobportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.entity.Recruiter;

public interface RecruiterRepository
        extends JpaRepository<Recruiter, Long> {

    Optional<Recruiter> findByUserId(Long userId);

    Optional<Recruiter> findByUserEmail(String email);
}