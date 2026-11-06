package com.launchpoint.application;

import java.time.LocalDateTime;

public class ApplicationResponse {

    private Long applicationId;
    private Long studentProfileId;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String studentName;
    private ApplicationStatus status;
    private Double matchScore;
    private LocalDateTime appliedAt;

    public ApplicationResponse(
            Long applicationId,
            Long studentProfileId,
            Long jobId,
            String jobTitle,
            String companyName,
            String studentName,
            ApplicationStatus status,
            Double matchScore,
            LocalDateTime appliedAt
    ) {
        this.applicationId = applicationId;
        this.studentProfileId = studentProfileId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.studentName = studentName;
        this.status = status;
        this.matchScore = matchScore;
        this.appliedAt = appliedAt;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public Long getStudentProfileId() {
        return studentProfileId;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getStudentName() {
        return studentName;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public Double getMatchScore() {
        return matchScore;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }
}