package com.jobportal.dto;

import jakarta.validation.constraints.NotBlank;

public class RecruiterRequest {

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Designation is required")
    private String designation;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}