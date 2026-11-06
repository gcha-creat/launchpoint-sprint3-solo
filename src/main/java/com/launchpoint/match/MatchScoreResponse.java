package com.launchpoint.match;

import java.util.List;

public class MatchScoreResponse {

    private Long studentProfileId;
    private Long jobId;
    private String jobTitle;
    private double matchScore;
    private List<MatchCriterionResult> criteria;
    private String summary;

    public MatchScoreResponse(
            Long studentProfileId,
            Long jobId,
            String jobTitle,
            double matchScore,
            List<MatchCriterionResult> criteria,
            String summary
    ) {
        this.studentProfileId = studentProfileId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.matchScore = matchScore;
        this.criteria = criteria;
        this.summary = summary;
    }

    public Long getStudentProfileId() {
        return studentProfileId;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public List<MatchCriterionResult> getCriteria() {
        return criteria;
    }

    public String getSummary() {
        return summary;
    }
}