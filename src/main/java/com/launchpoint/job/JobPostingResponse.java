package com.launchpoint.job;

import java.time.LocalDateTime;

public class JobPostingResponse {

    private Long jobId;
    private Long employerProfileId;
    private String companyName;
    private String title;
    private String description;
    private String skills;
    private String field;
    private String jobType;
    private String workMode;
    private String location;
    private String wage;
    private boolean active;
    private LocalDateTime createdAt;

    public JobPostingResponse(
            Long jobId,
            Long employerProfileId,
            String companyName,
            String title,
            String description,
            String skills,
            String field,
            String jobType,
            String workMode,
            String location,
            String wage,
            boolean active,
            LocalDateTime createdAt
    ) {
        this.jobId = jobId;
        this.employerProfileId = employerProfileId;
        this.companyName = companyName;
        this.title = title;
        this.description = description;
        this.skills = skills;
        this.field = field;
        this.jobType = jobType;
        this.workMode = workMode;
        this.location = location;
        this.wage = wage;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getJobId() {
        return jobId;
    }

    public Long getEmployerProfileId() {
        return employerProfileId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSkills() {
        return skills;
    }

    public String getField() {
        return field;
    }

    public String getJobType() {
        return jobType;
    }

    public String getWorkMode() {
        return workMode;
    }

    public String getLocation() {
        return location;
    }

    public String getWage() {
        return wage;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}