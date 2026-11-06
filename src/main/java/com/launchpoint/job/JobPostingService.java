package com.launchpoint.job;

import com.launchpoint.employer.EmployerProfile;
import com.launchpoint.employer.EmployerProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final EmployerProfileRepository employerProfileRepository;

    public JobPostingService(
            JobPostingRepository jobPostingRepository,
            EmployerProfileRepository employerProfileRepository
    ) {
        this.jobPostingRepository = jobPostingRepository;
        this.employerProfileRepository = employerProfileRepository;
    }

    public JobPostingResponse createJob(
            Long employerUserId,
            JobPostingRequest request
    ) {
        EmployerProfile employerProfile =
                employerProfileRepository
                        .findByUserId(employerUserId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Employer profile not found"
                                )
                        );

        JobPosting job = new JobPosting();

        job.setEmployerProfile(employerProfile);
        applyRequest(job, request);

        JobPosting savedJob =
                jobPostingRepository.save(job);

        return toResponse(savedJob);
    }

    public JobPostingResponse getJob(Long jobId) {
        JobPosting job = jobPostingRepository
                .findById(jobId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Job posting not found"
                        )
                );

        return toResponse(job);
    }

    public JobPostingResponse updateJob(
            Long jobId,
            JobPostingRequest request
    ) {
        JobPosting job = jobPostingRepository
                .findById(jobId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Job posting not found"
                        )
                );

        applyRequest(job, request);

        return toResponse(
                jobPostingRepository.save(job)
        );
    }

    public List<JobPostingResponse> getAvailableJobs() {
        return jobPostingRepository
                .findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<JobPostingResponse> searchByKeyword(
            String keyword
    ) {
        return jobPostingRepository
                .findByActiveTrueAndTitleContainingIgnoreCase(
                        keyword
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<JobPostingResponse> searchByLocation(
            String location
    ) {
        return jobPostingRepository
                .findByActiveTrueAndLocationContainingIgnoreCase(
                        location
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<JobPostingResponse> searchByField(
            String field
    ) {
        return jobPostingRepository
                .findByActiveTrueAndFieldContainingIgnoreCase(
                        field
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public JobPostingResponse changeStatus(
            Long jobId,
            boolean active
    ) {
        JobPosting job = jobPostingRepository
                .findById(jobId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Job posting not found"
                        )
                );

        job.setActive(active);

        return toResponse(
                jobPostingRepository.save(job)
        );
    }

    private void applyRequest(
            JobPosting job,
            JobPostingRequest request
    ) {
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setSkills(request.getSkills());
        job.setField(request.getField());
        job.setJobType(request.getJobType());
        job.setWorkMode(request.getWorkMode());
        job.setLocation(request.getLocation());
        job.setWage(request.getWage());
    }

    private JobPostingResponse toResponse(JobPosting job) {
        return new JobPostingResponse(
                job.getId(),
                job.getEmployerProfile().getId(),
                job.getEmployerProfile().getCompanyName(),
                job.getTitle(),
                job.getDescription(),
                job.getSkills(),
                job.getField(),
                job.getJobType(),
                job.getWorkMode(),
                job.getLocation(),
                job.getWage(),
                job.isActive(),
                job.getCreatedAt()
        );
    }
}