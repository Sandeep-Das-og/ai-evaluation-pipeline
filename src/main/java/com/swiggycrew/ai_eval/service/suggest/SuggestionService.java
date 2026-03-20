package com.swiggycrew.ai_eval.service.suggest;

import com.swiggycrew.ai_eval.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuggestionService {
    public List<Suggestion> generateSuggestions(Conversation conversation, Evaluation evaluation) {
        List<Suggestion> suggestions = new ArrayList<>();

        if (evaluation.getToolAccuracyScore() != null && evaluation.getToolAccuracyScore() < 0.8) {
            Suggestion suggestion = new Suggestion();
            suggestion.setType(SuggestionType.TOOL_SCHEMA);
            suggestion.setSuggestion("Improve tool parameter descriptions and add validation rules for missing fields.");
            suggestion.setRationale("Low tool accuracy indicates parameter extraction errors.");
            suggestion.setConfidence(0.7);
            suggestions.add(suggestion);
        }

        if (evaluation.getCoherenceScore() != null && evaluation.getCoherenceScore() < 0.8) {
            Suggestion suggestion = new Suggestion();
            suggestion.setType(SuggestionType.PROMPT);
            suggestion.setSuggestion("Add explicit context-carryover instructions for multi-turn conversations > 5 turns.");
            suggestion.setRationale("Coherence dips as turn count increases.");
            suggestion.setConfidence(0.65);
            suggestions.add(suggestion);
        }

        if (evaluation.getResponseQualityScore() != null && evaluation.getResponseQualityScore() < 0.7) {
            Suggestion suggestion = new Suggestion();
            suggestion.setType(SuggestionType.PROMPT);
            suggestion.setSuggestion("Add explicit formatting and completeness checklist to the system prompt.");
            suggestion.setRationale("Response quality is below target and aligns with user feedback.");
            suggestion.setConfidence(0.6);
            suggestions.add(suggestion);
        }

        boolean latencyIssue = evaluation.getIssues().stream().anyMatch(issue -> "latency".equals(issue.getType()));
        if (latencyIssue) {
            Suggestion suggestion = new Suggestion();
            suggestion.setType(SuggestionType.VALIDATION);
            suggestion.setSuggestion("Introduce response-time SLO checks and circuit-breaker rules for slow tools.");
            suggestion.setRationale("Latency exceeded threshold for this conversation.");
            suggestion.setConfidence(0.55);
            suggestions.add(suggestion);
        }

        return suggestions;
    }
}
