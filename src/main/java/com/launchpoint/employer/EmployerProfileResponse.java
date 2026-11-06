package com.launchpoint.employer;

public class EmployerProfileResponse {

    private Long profileId;
    private Long userId;
    private String email;
    private String companyName;
    private String contactName;
    private String phone;
    private String website;
    private String location;
    private String description;

    public EmployerProfileResponse(
            Long profileId,
            Long userId,
            String email,
            String companyName,
            String contactName,
            String phone,
            String website,
            String location,
            String description
    ) {
        this.profileId = profileId;
        this.userId = userId;
        this.email = email;
        this.companyName = companyName;
        this.contactName = contactName;
        this.phone = phone;
        this.website = website;
        this.location = location;
        this.description = description;
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

    public String getCompanyName() {
        return companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
}