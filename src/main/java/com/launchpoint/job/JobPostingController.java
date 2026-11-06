package com.launchpoint.job;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    public JobPostingController(
            JobPostingService jobPostingService
    ) {
        this.jobPostingService = jobPostingService;
    }

    @PostMapping("/employer/{userId}")
    public ResponseEntity<?> createJob(
            @PathVariable Long userId,
            @Valid @RequestBody JobPostingRequest request
    ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            jobPostingService.createJob(
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

    @GetMapping
    public List<JobPostingResponse> getAvailableJobs() {
        return jobPostingService.getAvailableJobs();
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJob(
            @PathVariable Long jobId
    ) {
        try {
            return ResponseEntity.ok(
                    jobPostingService.getJob(jobId)
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<?> updateJob(
            @PathVariable Long jobId,
            @Valid @RequestBody JobPostingRequest request
    ) {
        try {
            return ResponseEntity.ok(
                    jobPostingService.updateJob(
                            jobId,
                            request
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public List<JobPostingResponse> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String field
    ) {
        if (keyword != null && !keyword.isBlank()) {
            return jobPostingService.searchByKeyword(keyword);
        }

        if (location != null && !location.isBlank()) {
            return jobPostingService.searchByLocation(location);
        }

        if (field != null && !field.isBlank()) {
            return jobPostingService.searchByField(field);
        }

        return jobPostingService.getAvailableJobs();
    }

    @PatchMapping("/{jobId}/status")
    public ResponseEntity<?> changeStatus(
            @PathVariable Long jobId,
            @RequestParam boolean active
    ) {
        try {
            return ResponseEntity.ok(
                    jobPostingService.changeStatus(
                            jobId,
                            active
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}