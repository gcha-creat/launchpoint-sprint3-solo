package com.launchpoint.employer;

import com.launchpoint.user.User;
import com.launchpoint.user.UserRepository;
import com.launchpoint.user.UserRole;
import org.springframework.stereotype.Service;

@Service
public class EmployerProfileService {

    private final EmployerProfileRepository employerProfileRepository;
    private final UserRepository userRepository;

    public EmployerProfileService(
            EmployerProfileRepository employerProfileRepository,
            UserRepository userRepository
    ) {
        this.employerProfileRepository = employerProfileRepository;
        this.userRepository = userRepository;
    }

    public EmployerProfileResponse createProfile(
            Long userId,
            EmployerProfileRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        if (user.getRole() != UserRole.EMPLOYER) {
            throw new IllegalArgumentException(
                    "Only employer users can create an employer profile"
            );
        }

        if (employerProfileRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException(
                    "Employer profile already exists"
            );
        }

        EmployerProfile profile = new EmployerProfile();

        profile.setUser(user);
        applyRequest(profile, request);

        EmployerProfile savedProfile =
                employerProfileRepository.save(profile);

        return toResponse(savedProfile);
    }

    public EmployerProfileResponse getProfile(Long userId) {

        EmployerProfile profile = employerProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employer profile not found"
                        )
                );

        return toResponse(profile);
    }

    public EmployerProfileResponse updateProfile(
            Long userId,
            EmployerProfileRequest request
    ) {
        EmployerProfile profile = employerProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employer profile not found"
                        )
                );

        applyRequest(profile, request);

        EmployerProfile updatedProfile =
                employerProfileRepository.save(profile);

        return toResponse(updatedProfile);
    }

    private void applyRequest(
            EmployerProfile profile,
            EmployerProfileRequest request
    ) {
        profile.setCompanyName(request.getCompanyName());
        profile.setContactName(request.getContactName());
        profile.setPhone(request.getPhone());
        profile.setWebsite(request.getWebsite());
        profile.setLocation(request.getLocation());
        profile.setDescription(request.getDescription());
    }

    private EmployerProfileResponse toResponse(
            EmployerProfile profile
    ) {
        return new EmployerProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getUser().getEmail(),
                profile.getCompanyName(),
                profile.getContactName(),
                profile.getPhone(),
                profile.getWebsite(),
                profile.getLocation(),
                profile.getDescription()
        );
    }
}