package com.launchpoint.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    boolean existsByStudentProfileIdAndJobPostingId(
            Long studentProfileId,
            Long jobPostingId
    );

    List<Application> findByStudentProfileUserId(
            Long userId
    );

    List<Application> findByJobPostingId(
            Long jobId
    );
}