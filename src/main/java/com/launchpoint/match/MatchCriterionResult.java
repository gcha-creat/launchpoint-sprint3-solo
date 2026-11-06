package com.launchpoint.match;

public class MatchCriterionResult {

    private String criterion;
    private double score;
    private double maxScore;
    private boolean matched;
    private String explanation;

    public MatchCriterionResult(
            String criterion,
            double score,
            double maxScore,
            boolean matched,
            String explanation
    ) {
        this.criterion = criterion;
        this.score = score;
        this.maxScore = maxScore;
        this.matched = matched;
        this.explanation = explanation;
    }

    public String getCriterion() {
        return criterion;
    }

    public double getScore() {
        return score;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public boolean isMatched() {
        return matched;
    }

    public String getExplanation() {
        return explanation;
    }
}