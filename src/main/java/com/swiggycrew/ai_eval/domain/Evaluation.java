package com.swiggycrew.ai_eval.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "evaluations")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String evaluationId;

    private Double overallScore;
    private Double responseQualityScore;
    private Double toolAccuracyScore;
    private Double coherenceScore;

    private Double selectionAccuracy;
    private Double parameterAccuracy;
    private Boolean executionSuccess;

    private Double annotatorAgreement;

    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IssueDetected> issues = new LinkedHashSet<>();

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Suggestion> suggestions = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

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

    public Double getAnnotatorAgreement() {
        return annotatorAgreement;
    }

    public void setAnnotatorAgreement(Double annotatorAgreement) {
        this.annotatorAgreement = annotatorAgreement;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Set<IssueDetected> getIssues() {
        return issues;
    }

    public void setIssues(Set<IssueDetected> issues) {
        this.issues = issues;
    }

    public Set<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(Set<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }
}
