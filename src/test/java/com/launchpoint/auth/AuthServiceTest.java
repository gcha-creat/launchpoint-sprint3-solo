package com.launchpoint.auth;

import com.launchpoint.user.User;
import com.launchpoint.user.UserRepository;
import com.launchpoint.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(userRepository, passwordEncoder);
    }

    @Test
    void registerSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("student@test.com");
        request.setPassword("password123");
        request.setRole(UserRole.STUDENT);

        when(userRepository.existsByEmail("student@test.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("hashedPassword");

        User savedUser = new User(
                "student@test.com",
                "hashedPassword",
                UserRole.STUDENT
        );

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        AuthResponse response = authService.register(request);

        assertEquals("Registration successful", response.getMessage());
        assertEquals("student@test.com", response.getEmail());
        assertEquals("STUDENT", response.getRole());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerDuplicateEmailFails() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("student@test.com");
        request.setPassword("password123");
        request.setRole(UserRole.STUDENT);

        when(userRepository.existsByEmail("student@test.com"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(request)
        );

        assertEquals(
                "Email is already registered",
                exception.getMessage()
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setEmail("student@test.com");
        request.setPassword("password123");

        User user = new User(
                "student@test.com",
                "hashedPassword",
                UserRole.STUDENT
        );

        when(userRepository.findByEmail("student@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "password123",
                "hashedPassword"
        )).thenReturn(true);

        AuthResponse response = authService.login(request);

        assertEquals("Login successful", response.getMessage());
        assertEquals("student@test.com", response.getEmail());
        assertEquals("STUDENT", response.getRole());
    }

    @Test
    void loginWrongPasswordFails() {
        LoginRequest request = new LoginRequest();
        request.setEmail("student@test.com");
        request.setPassword("wrongpassword");

        User user = new User(
                "student@test.com",
                "hashedPassword",
                UserRole.STUDENT
        );

        when(userRepository.findByEmail("student@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrongpassword",
                "hashedPassword"
        )).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(request)
        );

        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );
    }
}