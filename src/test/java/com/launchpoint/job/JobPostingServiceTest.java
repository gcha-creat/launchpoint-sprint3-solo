package com.launchpoint.job;

import com.launchpoint.employer.EmployerProfile;
import com.launchpoint.employer.EmployerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobPostingServiceTest {

    @Mock
    private JobPostingRepository jobPostingRepository;

    @Mock
    private EmployerProfileRepository employerProfileRepository;

    private JobPostingService jobPostingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        jobPostingService = new JobPostingService(
                jobPostingRepository,
                employerProfileRepository
        );
    }

    @Test
    void createJobSuccess() {
        EmployerProfile employerProfile = createEmployerProfile();

        when(employerProfileRepository.findByUserId(2L))
                .thenReturn(Optional.of(employerProfile));

        when(jobPostingRepository.save(any(JobPosting.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobPostingResponse response =
                jobPostingService.createJob(
                        2L,
                        createRequest()
                );

        assertEquals(
                "Software Engineering Intern",
                response.getTitle()
        );

        assertEquals(
                "LaunchPoint Tech",
                response.getCompanyName()
        );

        verify(jobPostingRepository)
                .save(any(JobPosting.class));
    }

    @Test
    void createJobWithoutEmployerProfileFails() {
        when(employerProfileRepository.findByUserId(2L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> jobPostingService.createJob(
                                2L,
                                createRequest()
                        )
                );

        assertEquals(
                "Employer profile not found",
                exception.getMessage()
        );
    }

    @Test
    void getAvailableJobsSuccess() {
        JobPosting job = createJobPosting();

        when(jobPostingRepository.findByActiveTrue())
                .thenReturn(List.of(job));

        List<JobPostingResponse> jobs =
                jobPostingService.getAvailableJobs();

        assertEquals(1, jobs.size());

        assertEquals(
                "Software Engineering Intern",
                jobs.get(0).getTitle()
        );
    }

    @Test
    void searchByLocationSuccess() {
        JobPosting job = createJobPosting();

        when(
                jobPostingRepository
                        .findByActiveTrueAndLocationContainingIgnoreCase(
                                "Jersey"
                        )
        ).thenReturn(List.of(job));

        List<JobPostingResponse> jobs =
                jobPostingService.searchByLocation(
                        "Jersey"
                );

        assertEquals(1, jobs.size());

        assertEquals(
                "New Jersey",
                jobs.get(0).getLocation()
        );
    }

    @Test
    void changeStatusSuccess() {
        JobPosting job = createJobPosting();

        when(jobPostingRepository.findById(1L))
                .thenReturn(Optional.of(job));

        when(jobPostingRepository.save(any(JobPosting.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobPostingResponse response =
                jobPostingService.changeStatus(
                        1L,
                        false
                );

        assertFalse(response.isActive());
    }

    private EmployerProfile createEmployerProfile() {
        EmployerProfile employerProfile =
                new EmployerProfile();

        employerProfile.setCompanyName(
                "LaunchPoint Tech"
        );

        return employerProfile;
    }

    private JobPosting createJobPosting() {
        EmployerProfile employerProfile =
                createEmployerProfile();

        JobPosting job = new JobPosting();

        job.setEmployerProfile(employerProfile);
        job.setTitle(
                "Software Engineering Intern"
        );
        job.setDescription(
                "Assist with web application development and testing."
        );
        job.setSkills(
                "Java, Spring Boot, MySQL"
        );
        job.setField(
                "Computer Science"
        );
        job.setJobType(
                "Internship"
        );
        job.setWorkMode(
                "Hybrid"
        );
        job.setLocation(
                "New Jersey"
        );
        job.setWage(
                "$20/hour"
        );
        job.setActive(true);

        return job;
    }

    private JobPostingRequest createRequest() {
        JobPostingRequest request =
                new JobPostingRequest();

        request.setTitle(
                "Software Engineering Intern"
        );
        request.setDescription(
                "Assist with web application development and testing."
        );
        request.setSkills(
                "Java, Spring Boot, MySQL"
        );
        request.setField(
                "Computer Science"
        );
        request.setJobType(
                "Internship"
        );
        request.setWorkMode(
                "Hybrid"
        );
        request.setLocation(
                "New Jersey"
        );
        request.setWage(
                "$20/hour"
        );

        return request;
    }
}