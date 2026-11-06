package com.launchpoint.employer;

import com.launchpoint.user.User;
import com.launchpoint.user.UserRepository;
import com.launchpoint.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployerProfileServiceTest {

    @Mock
    private EmployerProfileRepository employerProfileRepository;

    @Mock
    private UserRepository userRepository;

    private EmployerProfileService employerProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employerProfileService = new EmployerProfileService(
                employerProfileRepository,
                userRepository
        );
    }

    @Test
    void createProfileSuccess() {
        User employer = new User(
                "employer@test.com",
                "hashedPassword",
                UserRole.EMPLOYER
        );

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(employer));

        when(employerProfileRepository.existsByUserId(2L))
                .thenReturn(false);

        when(employerProfileRepository.save(any(EmployerProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EmployerProfileResponse response =
                employerProfileService.createProfile(
                        2L,
                        createRequest()
                );

        assertEquals(
                "employer@test.com",
                response.getEmail()
        );

        assertEquals(
                "LaunchPoint Tech",
                response.getCompanyName()
        );

        verify(employerProfileRepository)
                .save(any(EmployerProfile.class));
    }

    @Test
    void createProfileForStudentFails() {
        User student = new User(
                "student@test.com",
                "hashedPassword",
                UserRole.STUDENT
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(student));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> employerProfileService.createProfile(
                                1L,
                                createRequest()
                        )
                );

        assertEquals(
                "Only employer users can create an employer profile",
                exception.getMessage()
        );
    }

    @Test
    void duplicateProfileFails() {
        User employer = new User(
                "employer@test.com",
                "hashedPassword",
                UserRole.EMPLOYER
        );

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(employer));

        when(employerProfileRepository.existsByUserId(2L))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> employerProfileService.createProfile(
                                2L,
                                createRequest()
                        )
                );

        assertEquals(
                "Employer profile already exists",
                exception.getMessage()
        );
    }

    @Test
    void updateProfileSuccess() {
        User employer = new User(
                "employer@test.com",
                "hashedPassword",
                UserRole.EMPLOYER
        );

        EmployerProfile existingProfile =
                new EmployerProfile();

        existingProfile.setUser(employer);
        existingProfile.setCompanyName("LaunchPoint Tech");
        existingProfile.setContactName("Jeff Smith");

        when(employerProfileRepository.findByUserId(2L))
                .thenReturn(Optional.of(existingProfile));

        when(employerProfileRepository.save(
                any(EmployerProfile.class)
        )).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        EmployerProfileRequest request = createRequest();
        request.setLocation("New York");

        EmployerProfileResponse response =
                employerProfileService.updateProfile(
                        2L,
                        request
                );

        assertEquals(
                "New York",
                response.getLocation()
        );
    }

    private EmployerProfileRequest createRequest() {
        EmployerProfileRequest request =
                new EmployerProfileRequest();

        request.setCompanyName("LaunchPoint Tech");
        request.setContactName("Jeff Smith");
        request.setPhone("201-555-0202");
        request.setWebsite(
                "https://launchpoint.example.com"
        );
        request.setLocation("New Jersey");
        request.setDescription(
                "Technology company offering internship opportunities."
        );

        return request;
    }
}