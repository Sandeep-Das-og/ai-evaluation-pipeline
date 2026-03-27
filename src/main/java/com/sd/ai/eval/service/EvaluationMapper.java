package com.sd.ai.eval.service;

import com.sd.ai.eval.domain.Evaluation;
import com.sd.ai.eval.domain.IssueDetected;
import com.sd.ai.eval.domain.Suggestion;
import com.sd.ai.eval.model.EvaluationResponse;
import org.hibernate.Hibernate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class EvaluationMapper {
    private EvaluationMapper() {}

    public static EvaluationResponse toResponse(Evaluation evaluation) {
        EvaluationResponse response = new EvaluationResponse();
        response.setEvaluationId(evaluation.getEvaluationId());
        if (evaluation.getConversation() != null && Hibernate.isInitialized(evaluation.getConversation())) {
            response.setConversationId(evaluation.getConversation().getConversationId());
        }

        EvaluationResponse.Scores scores = new EvaluationResponse.Scores();
        scores.setOverall(evaluation.getOverallScore());
        scores.setResponseQuality(evaluation.getResponseQualityScore());
        scores.setToolAccuracy(evaluation.getToolAccuracyScore());
        scores.setCoherence(evaluation.getCoherenceScore());
        response.setScores(scores);

        EvaluationResponse.ToolEvaluation toolEvaluation = new EvaluationResponse.ToolEvaluation();
        toolEvaluation.setSelectionAccuracy(evaluation.getSelectionAccuracy());
        toolEvaluation.setParameterAccuracy(evaluation.getParameterAccuracy());
        toolEvaluation.setExecutionSuccess(evaluation.getExecutionSuccess());
        response.setToolEvaluation(toolEvaluation);

        EvaluationResponse.Routing routing = new EvaluationResponse.Routing();
        routing.setAgreement(evaluation.getAnnotatorAgreement());
        Double avgConfidence = FeedbackRoutingUtil.averageConfidence(evaluation);
        routing.setAverageConfidence(avgConfidence);
        routing.setDecision(FeedbackRoutingUtil.routeDecision(evaluation.getAnnotatorAgreement(), avgConfidence));
        response.setRouting(routing);

        Collection<IssueDetected> issues = Hibernate.isInitialized(evaluation.getIssues()) ? evaluation.getIssues() : List.of();
        response.setIssuesDetected(issues.stream().map(issue -> {
            EvaluationResponse.IssueDto dto = new EvaluationResponse.IssueDto();
            dto.setType(issue.getType());
            dto.setSeverity(issue.getSeverity() == null ? null : issue.getSeverity().name().toLowerCase());
            dto.setDescription(issue.getDescription());
            return dto;
        }).collect(Collectors.toList()));

        Collection<Suggestion> suggestions = Hibernate.isInitialized(evaluation.getSuggestions()) ? evaluation.getSuggestions() : List.of();
        response.setImprovementSuggestions(suggestions.stream().map(suggestion -> {
            EvaluationResponse.SuggestionDto dto = new EvaluationResponse.SuggestionDto();
            dto.setType(suggestion.getType() == null ? null : suggestion.getType().name().toLowerCase());
            dto.setSuggestion(suggestion.getSuggestion());
            dto.setRationale(suggestion.getRationale());
            dto.setConfidence(suggestion.getConfidence());
            return dto;
        }).collect(Collectors.toList()));

        return response;
    }
}
