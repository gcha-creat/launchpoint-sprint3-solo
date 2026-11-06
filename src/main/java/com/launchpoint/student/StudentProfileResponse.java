package com.launchpoint.student;

public class StudentProfileResponse {

    private Long profileId;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String major;
    private String skills;
    private String location;
    private String personalStatement;
    private String resumeUrl;

    public StudentProfileResponse(
            Long profileId,
            Long userId,
            String email,
            String firstName,
            String lastName,
            String phone,
            String major,
            String skills,
            String location,
            String personalStatement,
            String resumeUrl
    ) {
        this.profileId = profileId;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.major = major;
        this.skills = skills;
        this.location = location;
        this.personalStatement = personalStatement;
        this.resumeUrl = resumeUrl;
    }

    public Long getProfileId() {
        return profileId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getMajor() {
        return major;
    }

    public String getSkills() {
        return skills;
    }

    public String getLocation() {
        return location;
    }

    public String getPersonalStatement() {
        return personalStatement;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }
}