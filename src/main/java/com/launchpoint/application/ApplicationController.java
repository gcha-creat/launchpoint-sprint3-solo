package com.launchpoint.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(
            ApplicationService applicationService
    ) {
        this.applicationService = applicationService;
    }

    @PostMapping("/student/{studentUserId}/job/{jobId}")
    public ResponseEntity<?> apply(
            @PathVariable Long studentUserId,
            @PathVariable Long jobId
    ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            applicationService.apply(
                                    studentUserId,
                                    jobId
                            )
                    );

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/student/{studentUserId}")
    public List<ApplicationResponse> getStudentApplications(
            @PathVariable Long studentUserId
    ) {
        return applicationService
                .getStudentApplications(studentUserId);
    }

    @GetMapping("/job/{jobId}")
    public List<ApplicationResponse> getJobApplications(
            @PathVariable Long jobId
    ) {
        return applicationService
                .getJobApplications(jobId);
    }

    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus status
    ) {
        try {
            return ResponseEntity.ok(
                    applicationService.updateStatus(
                            applicationId,
                            status
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}