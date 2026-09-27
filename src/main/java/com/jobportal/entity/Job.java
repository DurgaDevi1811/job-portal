package com.jobportal.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Job
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String location;

    private Double salary;

    private String skills;

    private String jobType;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

    @JsonIgnore
    @OneToMany(
        mappedBy = "job",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Application> applications = new ArrayList<>();

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public Double getSalary()
    {
        return salary;
    }

    public void setSalary(Double salary)
    {
        this.salary = salary;
    }

    public String getSkills()
    {
        return skills;
    }

    public void setSkills(String skills)
    {
        this.skills = skills;
    }

    public String getJobType()
    {
        return jobType;
    }

    public void setJobType(String jobType)
    {
        this.jobType = jobType;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public Recruiter getRecruiter()
    {
        return recruiter;
    }

    public void setRecruiter(Recruiter recruiter)
    {
        this.recruiter = recruiter;
    }

    public List<Application> getApplications()
    {
        return applications;
    }

    public void setApplications(List<Application> applications)
    {
        this.applications = applications;
    }
}