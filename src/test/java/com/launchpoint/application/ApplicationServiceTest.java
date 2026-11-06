package com.launchpoint.application;

import com.launchpoint.employer.EmployerProfile;
import com.launchpoint.job.JobPosting;
import com.launchpoint.job.JobPostingRepository;
import com.launchpoint.student.StudentProfile;
import com.launchpoint.student.StudentProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private StudentProfileRepository studentProfileRepository;

    @Mock
    private JobPostingRepository jobPostingRepository;

    private ApplicationService applicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        applicationService = new ApplicationService(
                applicationRepository,
                studentProfileRepository,
                jobPostingRepository
        );
    }

    @Test
    void applySuccess() {
        StudentProfile student = createStudentProfile();
        JobPosting job = createJobPosting();

        when(studentProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(student));

        when(jobPostingRepository.findById(1L))
                .thenReturn(Optional.of(job));

        when(applicationRepository
                .existsByStudentProfileIdAndJobPostingId(
                        student.getId(),
                        job.getId()
                ))
                .thenReturn(false);

        when(applicationRepository.save(any(Application.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ApplicationResponse response =
                applicationService.apply(1L, 1L);

        assertEquals(
                ApplicationStatus.PENDING,
                response.getStatus()
        );

        assertEquals(
                "Software Engineering Intern",
                response.getJobTitle()
        );

        assertEquals(
                "Giwoong Cha",
                response.getStudentName()
        );

        verify(applicationRepository)
                .save(any(Application.class));
    }

    @Test
    void duplicateApplicationFails() {
        StudentProfile student = createStudentProfile();
        JobPosting job = createJobPosting();

        when(studentProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(student));

        when(jobPostingRepository.findById(1L))
                .thenReturn(Optional.of(job));

        when(applicationRepository
                .existsByStudentProfileIdAndJobPostingId(
                        student.getId(),
                        job.getId()
                ))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> applicationService.apply(1L, 1L)
                );

        assertEquals(
                "Student has already applied for this job",
                exception.getMessage()
        );
    }

    @Test
    void inactiveJobFails() {
        StudentProfile student = createStudentProfile();
        JobPosting job = createJobPosting();

        job.setActive(false);

        when(studentProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(student));

        when(jobPostingRepository.findById(1L))
                .thenReturn(Optional.of(job));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> applicationService.apply(1L, 1L)
                );

        assertEquals(
                "Job posting is not active",
                exception.getMessage()
        );
    }

    @Test
    void getStudentApplicationsSuccess() {
        Application application = createApplication();

        when(applicationRepository
                .findByStudentProfileUserId(1L))
                .thenReturn(List.of(application));

        List<ApplicationResponse> applications =
                applicationService
                        .getStudentApplications(1L);

        assertEquals(1, applications.size());

        assertEquals(
                "Software Engineering Intern",
                applications.get(0).getJobTitle()
        );
    }

    @Test
    void updateStatusSuccess() {
        Application application = createApplication();

        when(applicationRepository.findById(1L))
                .thenReturn(Optional.of(application));

        when(applicationRepository.save(any(Application.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ApplicationResponse response =
                applicationService.updateStatus(
                        1L,
                        ApplicationStatus.REVIEWING
                );

        assertEquals(
                ApplicationStatus.REVIEWING,
                response.getStatus()
        );
    }

    private StudentProfile createStudentProfile() {
        StudentProfile student = new StudentProfile();

        student.setFirstName("Giwoong");
        student.setLastName("Cha");

        return student;
    }

    private EmployerProfile createEmployerProfile() {
        EmployerProfile employer = new EmployerProfile();
        employer.setCompanyName("LaunchPoint Tech");

        return employer;
    }

    private JobPosting createJobPosting() {
        JobPosting job = new JobPosting();

        job.setEmployerProfile(createEmployerProfile());
        job.setTitle("Software Engineering Intern");
        job.setActive(true);

        return job;
    }

    private Application createApplication() {
        Application application = new Application();

        application.setStudentProfile(
                createStudentProfile()
        );

        application.setJobPosting(
                createJobPosting()
        );

        application.setStatus(
                ApplicationStatus.PENDING
        );

        return application;
    }
}
