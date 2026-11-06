package com.launchpoint.match;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match")
public class MatchScoreController {

    private final MatchScoreService matchScoreService;

    public MatchScoreController(
            MatchScoreService matchScoreService
    ) {
        this.matchScoreService = matchScoreService;
    }

    @GetMapping("/student/{studentUserId}/job/{jobId}")
    public ResponseEntity<?> calculateMatch(
            @PathVariable Long studentUserId,
            @PathVariable Long jobId
    ) {
        try {
            return ResponseEntity.ok(
                    matchScoreService.calculateMatch(
                            studentUserId,
                            jobId
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}