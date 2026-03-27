package com.sd.ai.eval.service.eval.llm;

import com.sd.ai.eval.domain.Conversation;
import com.sd.ai.eval.domain.Feedback;
import com.sd.ai.eval.domain.Turn;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.eval.llm-judge.mode", havingValue = "stub", matchIfMissing = true)
public class StubLlmJudgeClient implements LlmJudgeClient {
    @Override
    public double score(Conversation conversation) {
        double heuristicScore = estimateResponseQuality(conversation);
        double ratingScore = extractRating(conversation);
        double finalScore = ratingScore < 0 ? heuristicScore : (0.7 * heuristicScore + 0.3 * ratingScore);
        return clamp(finalScore);
    }

    private double estimateResponseQuality(Conversation conversation) {
        String lastAssistant = null;
        for (Turn turn : conversation.getTurns()) {
            if (turn.getRole().name().equals("ASSISTANT")) {
                lastAssistant = turn.getContent();
            }
        }
        if (lastAssistant == null || lastAssistant.isBlank()) {
            return 0.2;
        }
        double score = Math.min(1.0, 0.4 + (lastAssistant.length() / 400.0));
        String lower = lastAssistant.toLowerCase();
        if (lower.contains("cannot") || lower.contains("can't") || lower.contains("unable")) {
            score -= 0.2;
        }
        if (lower.contains("here is") || lower.contains("sure") || lower.contains("happy to help")) {
            score += 0.05;
        }
        return clamp(score);
    }

    private double extractRating(Conversation conversation) {
        Feedback feedback = conversation.getFeedback();
        if (feedback == null || feedback.getUserRating() == null) {
            return -1;
        }
        return clamp(feedback.getUserRating() / 5.0);
    }

    private double clamp(double value) {
        if (value < 0) {
            return 0;
        }
        if (value > 1) {
            return 1;
        }
        return value;
    }
}
