package com.launchpoint.student;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-profiles")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    public StudentProfileController(
            StudentProfileService studentProfileService
    ) {
        this.studentProfileService = studentProfileService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> createProfile(
            @PathVariable Long userId,
            @Valid @RequestBody StudentProfileRequest request
    ) {
        try {
            StudentProfileResponse response =
                    studentProfileService.createProfile(
                            userId,
                            request
                    );

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
                    studentProfileService.getProfile(userId)
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
            @Valid @RequestBody StudentProfileRequest request
    ) {
        try {
            return ResponseEntity.ok(
                    studentProfileService.updateProfile(
                            userId,
                            request
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}