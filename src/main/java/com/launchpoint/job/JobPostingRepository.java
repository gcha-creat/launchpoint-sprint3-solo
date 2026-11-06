package com.launchpoint.job;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostingRepository
        extends JpaRepository<JobPosting, Long> {

    List<JobPosting> findByActiveTrue();

    List<JobPosting> findByEmployerProfileId(Long employerProfileId);

    List<JobPosting> findByActiveTrueAndTitleContainingIgnoreCase(
            String keyword
    );

    List<JobPosting> findByActiveTrueAndLocationContainingIgnoreCase(
            String location
    );

    List<JobPosting> findByActiveTrueAndFieldContainingIgnoreCase(
            String field
    );
}