package com.launchpoint.student;

import com.launchpoint.user.User;
import com.launchpoint.user.UserRepository;
import com.launchpoint.user.UserRole;
import org.springframework.stereotype.Service;

@Service
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    public StudentProfileService(
            StudentProfileRepository studentProfileRepository,
            UserRepository userRepository
    ) {
        this.studentProfileRepository = studentProfileRepository;
        this.userRepository = userRepository;
    }

    public StudentProfileResponse createProfile(
            Long userId,
            StudentProfileRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        if (user.getRole() != UserRole.STUDENT) {
            throw new IllegalArgumentException(
                    "Only student users can create a student profile"
            );
        }

        if (studentProfileRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException(
                    "Student profile already exists"
            );
        }

        StudentProfile profile = new StudentProfile();

        profile.setUser(user);
        applyRequest(profile, request);

        StudentProfile savedProfile =
                studentProfileRepository.save(profile);

        return toResponse(savedProfile);
    }

    public StudentProfileResponse getProfile(Long userId) {

        StudentProfile profile = studentProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Student profile not found"
                        )
                );

        return toResponse(profile);
    }

    public StudentProfileResponse updateProfile(
            Long userId,
            StudentProfileRequest request
    ) {
        StudentProfile profile = studentProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Student profile not found"
                        )
                );

        applyRequest(profile, request);

        StudentProfile updatedProfile =
                studentProfileRepository.save(profile);

        return toResponse(updatedProfile);
    }

    private void applyRequest(
            StudentProfile profile,
            StudentProfileRequest request
    ) {
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setPhone(request.getPhone());
        profile.setMajor(request.getMajor());
        profile.setSkills(request.getSkills());
        profile.setLocation(request.getLocation());
        profile.setPersonalStatement(
                request.getPersonalStatement()
        );
        profile.setResumeUrl(request.getResumeUrl());
    }

    private StudentProfileResponse toResponse(
            StudentProfile profile
    ) {
        return new StudentProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getUser().getEmail(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhone(),
                profile.getMajor(),
                profile.getSkills(),
                profile.getLocation(),
                profile.getPersonalStatement(),
                profile.getResumeUrl()
        );
    }
}