package com.jobportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jobportal.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByTitleContainingIgnoreCase(String title);
    List<Job> findByRecruiterId(Long recruiterId);
    @Query("""
        SELECT j FROM Job j
        WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(j.skills) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Job> searchJobs(
            @Param("keyword") String keyword,
            Pageable pageable);
}