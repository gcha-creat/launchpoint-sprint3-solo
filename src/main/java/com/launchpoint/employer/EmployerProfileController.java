package com.launchpoint.employer;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employer-profiles")
public class EmployerProfileController {

    private final EmployerProfileService employerProfileService;

    public EmployerProfileController(
            EmployerProfileService employerProfileService
    ) {
        this.employerProfileService = employerProfileService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> createProfile(
            @PathVariable Long userId,
            @Valid @RequestBody EmployerProfileRequest request
    ) {
        try {
            EmployerProfileResponse response =
                    employerProfileService.createProfile(userId, request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(
            @PathVariable Long userId
    ) {
        try {
            return ResponseEntity.ok(
                    employerProfileService.getProfile(userId)
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody EmployerProfileRequest request
    ) {
        try {
            return ResponseEntity.ok(
                    employerProfileService.updateProfile(userId, request)
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}