package com.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.entity.Application;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByCandidateId(Long candidateId);

    List<Application> findByJobId(Long jobId);

    boolean existsByCandidateIdAndJobId(
            Long candidateId,
            Long jobId);
}