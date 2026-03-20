package com.swiggycrew.ai_eval.model;

import java.util.ArrayList;
import java.util.List;

public class BatchEvaluationResult {
    private List<EvaluationResponse> evaluations = new ArrayList<>();
    private List<BatchEvaluationFailure> failures = new ArrayList<>();

    public List<EvaluationResponse> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<EvaluationResponse> evaluations) {
        this.evaluations = evaluations;
    }

    public List<BatchEvaluationFailure> getFailures() {
        return failures;
    }

    public void setFailures(List<BatchEvaluationFailure> failures) {
        this.failures = failures;
    }
}
