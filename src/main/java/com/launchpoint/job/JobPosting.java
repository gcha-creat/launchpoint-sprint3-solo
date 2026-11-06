package com.launchpoint.job;

import com.launchpoint.employer.EmployerProfile;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_postings")
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employer_profile_id", nullable = false)
    private EmployerProfile employerProfile;

    @Column(nullable = false)
    private String title;

    @Column(length = 3000)
    private String description;

    @Column(length = 1000)
    private String skills;

    private String field;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "work_mode")
    private String workMode;

    private String location;

    private String wage;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public JobPosting() {
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public EmployerProfile getEmployerProfile() {
        return employerProfile;
    }

    public void setEmployerProfile(EmployerProfile employerProfile) {
        this.employerProfile = employerProfile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}