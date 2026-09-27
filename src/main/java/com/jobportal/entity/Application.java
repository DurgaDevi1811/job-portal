package com.jobportal.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "candidate_id",
                "job_id"
            }
        )
    }
)
public class Application
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private String status;

    private LocalDateTime appliedAt;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Candidate getCandidate()
    {
        return candidate;
    }

    public void setCandidate(Candidate candidate)
    {
        this.candidate = candidate;
    }

    public Job getJob()
    {
        return job;
    }

    public void setJob(Job job)
    {
        this.job = job;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public LocalDateTime getAppliedAt()
    {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt)
    {
        this.appliedAt = appliedAt;
    }
}