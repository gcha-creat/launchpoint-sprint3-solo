package com.launchpoint.match;

import com.launchpoint.job.JobPosting;
import com.launchpoint.job.JobPostingRepository;
import com.launchpoint.student.StudentProfile;
import com.launchpoint.student.StudentProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MatchScoreService {

    private static final double SKILLS_WEIGHT = 40.0;
    private static final double MAJOR_WEIGHT = 35.0;
    private static final double LOCATION_WEIGHT = 25.0;

    private final StudentProfileRepository studentProfileRepository;
    private final JobPostingRepository jobPostingRepository;

    public MatchScoreService(
            StudentProfileRepository studentProfileRepository,
            JobPostingRepository jobPostingRepository
    ) {
        this.studentProfileRepository = studentProfileRepository;
        this.jobPostingRepository = jobPostingRepository;
    }

    public MatchScoreResponse calculateMatch(
            Long studentUserId,
            Long jobId
    ) {
        StudentProfile student =
                studentProfileRepository
                        .findByUserId(studentUserId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Student profile not found"
                                )
                        );

        JobPosting job =
                jobPostingRepository
                        .findById(jobId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Job posting not found"
                                )
                        );

        List<MatchCriterionResult> criteria =
                new ArrayList<>();

        MatchCriterionResult skillsResult =
                compareSkills(student, job);

        MatchCriterionResult majorResult =
                compareMajor(student, job);

        MatchCriterionResult locationResult =
                compareLocation(student, job);

        criteria.add(skillsResult);
        criteria.add(majorResult);
        criteria.add(locationResult);

        double totalScore =
                skillsResult.getScore()
                        + majorResult.getScore()
                        + locationResult.getScore();

        totalScore =
                Math.round(totalScore * 100.0) / 100.0;

        String summary = generateSummary(totalScore);

        return new MatchScoreResponse(
                student.getId(),
                job.getId(),
                job.getTitle(),
                totalScore,
                criteria,
                summary
        );
    }

    private MatchCriterionResult compareSkills(
            StudentProfile student,
            JobPosting job
    ) {
        String requiredSkills = job.getSkills();

        if (requiredSkills == null ||
                requiredSkills.isBlank()) {

            return new MatchCriterionResult(
                    "Skills",
                    SKILLS_WEIGHT,
                    SKILLS_WEIGHT,
                    true,
                    "The job does not specify required skills."
            );
        }

        String studentSkills =
                student.getSkills() == null
                        ? ""
                        : student.getSkills().toLowerCase();

        List<String> required =
                Arrays.stream(requiredSkills.split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .filter(skill -> !skill.isBlank())
                        .toList();

        long matchedCount =
                required.stream()
                        .filter(studentSkills::contains)
                        .count();

        double ratio =
                required.isEmpty()
                        ? 1.0
                        : (double) matchedCount
                        / required.size();

        double score =
                ratio * SKILLS_WEIGHT;

        boolean matched =
                matchedCount == required.size();

        String explanation =
                matchedCount
                        + " of "
                        + required.size()
                        + " required skills matched.";

        return new MatchCriterionResult(
                "Skills",
                Math.round(score * 100.0) / 100.0,
                SKILLS_WEIGHT,
                matched,
                explanation
        );
    }

    private MatchCriterionResult compareMajor(
            StudentProfile student,
            JobPosting job
    ) {
        String jobField = job.getField();

        if (jobField == null || jobField.isBlank()) {
            return new MatchCriterionResult(
                    "Major / Field",
                    MAJOR_WEIGHT,
                    MAJOR_WEIGHT,
                    true,
                    "The job does not specify a required field."
            );
        }

        String studentMajor =
                student.getMajor() == null
                        ? ""
                        : student.getMajor().trim().toLowerCase();

        String requiredField =
                jobField.trim().toLowerCase();

        boolean matched =
                studentMajor.equals(requiredField)
                        || studentMajor.contains(requiredField)
                        || requiredField.contains(studentMajor)
                        && !studentMajor.isBlank();

        double score =
                matched ? MAJOR_WEIGHT : 0.0;

        String explanation =
                matched
                        ? "Student major matches the job field."
                        : "Student major does not match the job field.";

        return new MatchCriterionResult(
                "Major / Field",
                score,
                MAJOR_WEIGHT,
                matched,
                explanation
        );
    }

    private MatchCriterionResult compareLocation(
            StudentProfile student,
            JobPosting job
    ) {
        String jobLocation = job.getLocation();

        if (jobLocation == null ||
                jobLocation.isBlank()) {

            return new MatchCriterionResult(
                    "Location",
                    LOCATION_WEIGHT,
                    LOCATION_WEIGHT,
                    true,
                    "The job does not specify a location."
            );
        }

        String studentLocation =
                student.getLocation() == null
                        ? ""
                        : student.getLocation()
                        .trim()
                        .toLowerCase();

        String requiredLocation =
                jobLocation.trim().toLowerCase();

        boolean matched =
                !studentLocation.isBlank()
                        && (
                        studentLocation.equals(requiredLocation)
                                || studentLocation.contains(requiredLocation)
                                || requiredLocation.contains(studentLocation)
                );

        double score =
                matched ? LOCATION_WEIGHT : 0.0;

        String explanation =
                matched
                        ? "Student location matches the job location."
                        : "Student location does not match the job location.";

        return new MatchCriterionResult(
                "Location",
                score,
                LOCATION_WEIGHT,
                matched,
                explanation
        );
    }

    private String generateSummary(double score) {

        if (score >= 80) {
            return "Strong match";
        }

        if (score >= 60) {
            return "Good match";
        }

        if (score >= 40) {
            return "Partial match";
        }

        return "Low match";
    }
}