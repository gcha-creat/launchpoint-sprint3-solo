package com.launchpoint.application;

import com.launchpoint.job.JobPosting;
import com.launchpoint.job.JobPostingRepository;
import com.launchpoint.student.StudentProfile;
import com.launchpoint.student.StudentProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final JobPostingRepository jobPostingRepository;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            StudentProfileRepository studentProfileRepository,
            JobPostingRepository jobPostingRepository
    ) {
        this.applicationRepository = applicationRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.jobPostingRepository = jobPostingRepository;
    }

    public ApplicationResponse apply(
            Long studentUserId,
            Long jobId
    ) {
        StudentProfile studentProfile =
                studentProfileRepository
                        .findByUserId(studentUserId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Student profile not found"
                                )
                        );

        JobPosting jobPosting =
                jobPostingRepository
                        .findById(jobId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Job posting not found"
                                )
                        );

        if (!jobPosting.isActive()) {
            throw new IllegalArgumentException(
                    "Job posting is not active"
            );
        }

        if (applicationRepository
                .existsByStudentProfileIdAndJobPostingId(
                        studentProfile.getId(),
                        jobPosting.getId()
                )) {

            throw new IllegalArgumentException(
                    "Student has already applied for this job"
            );
        }

        Application application = new Application();

        application.setStudentProfile(studentProfile);
        application.setJobPosting(jobPosting);
        application.setStatus(ApplicationStatus.PENDING);

        Application savedApplication =
                applicationRepository.save(application);

        return toResponse(savedApplication);
    }

    public List<ApplicationResponse> getStudentApplications(
            Long studentUserId
    ) {
        return applicationRepository
                .findByStudentProfileUserId(studentUserId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ApplicationResponse> getJobApplications(
            Long jobId
    ) {
        return applicationRepository
                .findByJobPostingId(jobId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ApplicationResponse updateStatus(
            Long applicationId,
            ApplicationStatus status
    ) {
        Application application =
                applicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Application not found"
                                )
                        );

        application.setStatus(status);

        return toResponse(
                applicationRepository.save(application)
        );
    }

    private ApplicationResponse toResponse(
            Application application
    ) {
        StudentProfile student =
                application.getStudentProfile();

        JobPosting job =
                application.getJobPosting();

        return new ApplicationResponse(
                application.getId(),
                student.getId(),
                job.getId(),
                job.getTitle(),
                job.getEmployerProfile().getCompanyName(),
                student.getFirstName()
                        + " "
                        + student.getLastName(),
                application.getStatus(),
                application.getMatchScore(),
                application.getAppliedAt()
        );
    }
}