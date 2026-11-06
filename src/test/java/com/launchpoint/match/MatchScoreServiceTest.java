package com.launchpoint.match;

import com.launchpoint.job.JobPosting;
import com.launchpoint.job.JobPostingRepository;
import com.launchpoint.student.StudentProfile;
import com.launchpoint.student.StudentProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchScoreServiceTest {

    @Mock
    private StudentProfileRepository studentProfileRepository;

    @Mock
    private JobPostingRepository jobPostingRepository;

    private MatchScoreService matchScoreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        matchScoreService = new MatchScoreService(
                studentProfileRepository,
                jobPostingRepository
        );
    }

    @Test
    void perfectMatchReturns100() {
        StudentProfile student = createStudent();
        JobPosting job = createJob();

        when(studentProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(student));

        when(jobPostingRepository.findById(1L))
                .thenReturn(Optional.of(job));

        MatchScoreResponse response =
                matchScoreService.calculateMatch(1L, 1L);

        assertEquals(100.0, response.getMatchScore());
        assertEquals("Strong match", response.getSummary());
        assertEquals(3, response.getCriteria().size());

        assertTrue(
                response.getCriteria()
                        .stream()
                        .allMatch(MatchCriterionResult::isMatched)
        );
    }

    @Test
    void partialSkillsMatchCalculatesCorrectScore() {
        StudentProfile student = createStudent();

        student.setSkills("Java");

        JobPosting job = createJob();

        when(studentProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(student));

        when(jobPostingRepository.findById(1L))
                .thenReturn(Optional.of(job));

        MatchScoreResponse response =
                matchScoreService.calculateMatch(1L, 1L);

        double expected =
                (40.0 / 3.0) + 35.0 + 25.0;

        expected =
                Math.round(expected * 100.0) / 100.0;

        assertEquals(
                expected,
                response.getMatchScore(),
                0.01
        );

        assertEquals(
                "Good match",
                response.getSummary()
        );
    }

    @Test
    void differentMajorAndLocationProducesLowerScore() {
        StudentProfile student = createStudent();

        student.setMajor("Business");
        student.setLocation("California");

        JobPosting job = createJob();

        when(studentProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(student));

        when(jobPostingRepository.findById(1L))
                .thenReturn(Optional.of(job));

        MatchScoreResponse response =
                matchScoreService.calculateMatch(1L, 1L);

        assertEquals(
                40.0,
                response.getMatchScore()
        );

        assertEquals(
                "Partial match",
                response.getSummary()
        );
    }

    @Test
    void missingStudentProfileFails() {
        when(studentProfileRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> matchScoreService.calculateMatch(
                                1L,
                                1L
                        )
                );

        assertEquals(
                "Student profile not found",
                exception.getMessage()
        );
    }

    @Test
    void missingJobPostingFails() {
        StudentProfile student = createStudent();

        when(studentProfileRepository.findByUserId(1L))
                .thenReturn(Optional.of(student));

        when(jobPostingRepository.findById(1L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> matchScoreService.calculateMatch(
                                1L,
                                1L
                        )
                );

        assertEquals(
                "Job posting not found",
                exception.getMessage()
        );
    }

    private StudentProfile createStudent() {
        StudentProfile student = new StudentProfile();

        student.setFirstName("Giwoong");
        student.setLastName("Cha");
        student.setMajor("Computer Science");
        student.setSkills(
                "Java, Spring Boot, MySQL, React"
        );
        student.setLocation("New Jersey");

        return student;
    }

    private JobPosting createJob() {
        JobPosting job = new JobPosting();

        job.setTitle(
                "Software Engineering Intern"
        );

        job.setSkills(
                "Java, Spring Boot, MySQL"
        );

        job.setField(
                "Computer Science"
        );

        job.setLocation(
                "New Jersey"
        );

        job.setActive(true);

        return job;
    }
}