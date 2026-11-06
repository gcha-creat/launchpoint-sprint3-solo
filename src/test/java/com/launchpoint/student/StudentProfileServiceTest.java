package com.launchpoint.student;

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

class StudentProfileServiceTest {

    @Mock
    private StudentProfileRepository studentProfileRepository;

    @Mock
    private UserRepository userRepository;

    private StudentProfileService studentProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        studentProfileService = new StudentProfileService(
                studentProfileRepository,
                userRepository
        );
    }

    @Test
    void createProfileSuccess() {
        User user = new User(
                "student@test.com",
                "hashedPassword",
                UserRole.STUDENT
        );

        StudentProfileRequest request = createRequest();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(studentProfileRepository.existsByUserId(1L))
                .thenReturn(false);

        when(studentProfileRepository.save(any(StudentProfile.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        StudentProfileResponse response =
                studentProfileService.createProfile(1L, request);

        assertEquals(
                "student@test.com",
                response.getEmail()
        );

        assertEquals(
                "Giwoong",
                response.getFirstName()
        );

        assertEquals(
                "Computer Science",
                response.getMajor()
        );

        verify(studentProfileRepository)
                .save(any(StudentProfile.class));
    }

    @Test
    void createProfileForEmployerFails() {
        User employer = new User(
                "employer@test.com",
                "hashedPassword",
                UserRole.EMPLOYER
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(employer));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> studentProfileService.createProfile(
                                1L,
                                createRequest()
                        )
                );

        assertEquals(
                "Only student users can create a student profile",
                exception.getMessage()
        );
    }

    @Test
    void duplicateProfileFails() {
        User user = new User(
                "student@test.com",
                "hashedPassword",
                UserRole.STUDENT
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(studentProfileRepository.existsByUserId(1L))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> studentProfileService.createProfile(
                                1L,
                                createRequest()
                        )
                );

        assertEquals(
                "Student profile already exists",
                exception.getMessage()
        );
    }

    @Test
    void updateProfileSuccess() {
        User user = new User(
                "student@test.com",
                "hashedPassword",
                UserRole.STUDENT
        );

        StudentProfile existingProfile =
                new StudentProfile();

        existingProfile.setUser(user);
        existingProfile.setFirstName("Giwoong");
        existingProfile.setLastName("Cha");

        when(studentProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(existingProfile));

        when(studentProfileRepository.save(
                any(StudentProfile.class)
        )).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        StudentProfileRequest request = createRequest();
        request.setSkills(
                "Java, Spring Boot, MySQL, React"
        );

        StudentProfileResponse response =
                studentProfileService.updateProfile(
                        1L,
                        request
                );

        assertEquals(
                "Java, Spring Boot, MySQL, React",
                response.getSkills()
        );
    }

    private StudentProfileRequest createRequest() {
        StudentProfileRequest request =
                new StudentProfileRequest();

        request.setFirstName("Giwoong");
        request.setLastName("Cha");
        request.setPhone("201-555-0101");
        request.setMajor("Computer Science");
        request.setSkills(
                "Java, Spring Boot, MySQL"
        );
        request.setLocation("New Jersey");
        request.setPersonalStatement(
                "Student seeking practical experience."
        );
        request.setResumeUrl(
                "https://example.com/resume.pdf"
        );

        return request;
    }
}