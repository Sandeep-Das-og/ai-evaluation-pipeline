package com.swiggycrew.ai_eval.service.eval;

import com.swiggycrew.ai_eval.domain.IssueDetected;
import java.util.ArrayList;
import java.util.List;

public class EvaluatorResult {
    private Double responseQualityScore;
    private Double toolAccuracyScore;
    private Double coherenceScore;
    private Double selectionAccuracy;
    private Double parameterAccuracy;
    private Boolean executionSuccess;
    private List<IssueDetected> issues = new ArrayList<>();

    public Double getResponseQualityScore() {
        return responseQualityScore;
    }

    public void setResponseQualityScore(Double responseQualityScore) {
        this.responseQualityScore = responseQualityScore;
    }

    public Double getToolAccuracyScore() {
        return toolAccuracyScore;
    }

    public void setToolAccuracyScore(Double toolAccuracyScore) {
        this.toolAccuracyScore = toolAccuracyScore;
    }

    public Double getCoherenceScore() {
        return coherenceScore;
    }

    public void setCoherenceScore(Double coherenceScore) {
        this.coherenceScore = coherenceScore;
    }

    public Double getSelectionAccuracy() {
        return selectionAccuracy;
    }

    public void setSelectionAccuracy(Double selectionAccuracy) {
        this.selectionAccuracy = selectionAccuracy;
    }

    public Double getParameterAccuracy() {
        return parameterAccuracy;
    }

    public void setParameterAccuracy(Double parameterAccuracy) {
        this.parameterAccuracy = parameterAccuracy;
    }

    public Boolean getExecutionSuccess() {
        return executionSuccess;
    }

    public void setExecutionSuccess(Boolean executionSuccess) {
        this.executionSuccess = executionSuccess;
    }

    public List<IssueDetected> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueDetected> issues) {
        this.issues = issues;
    }
}
