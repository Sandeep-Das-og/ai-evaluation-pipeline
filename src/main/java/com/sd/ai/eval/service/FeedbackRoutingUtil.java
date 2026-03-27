package com.sd.ai.eval.service;

import com.sd.ai.eval.domain.Evaluation;
import com.sd.ai.eval.domain.Feedback;

public final class FeedbackRoutingUtil {
    private FeedbackRoutingUtil() {}

    private static final double AGREEMENT_THRESHOLD = 0.7;
    private static final double CONFIDENCE_THRESHOLD = 0.6;

    public static String routeDecision(Double agreement, Double averageConfidence) {
        if (agreement != null && agreement < AGREEMENT_THRESHOLD) {
            return "HUMAN_REVIEW";
        }
        if (averageConfidence != null && averageConfidence < CONFIDENCE_THRESHOLD) {
            return "HUMAN_REVIEW";
        }
        return "AUTO_ACCEPT";
    }

    public static Double averageConfidence(Evaluation evaluation) {
        if (evaluation == null || evaluation.getConversation() == null) {
            return null;
        }
        Feedback feedback = evaluation.getConversation().getFeedback();
        if (feedback == null || feedback.getAnnotations() == null || feedback.getAnnotations().isEmpty()) {
            return null;
        }
        double total = 0;
        int count = 0;
        for (var annotation : feedback.getAnnotations()) {
            if (annotation.getConfidence() != null) {
                total += annotation.getConfidence();
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        return Math.round((total / count) * 100.0) / 100.0;
    }
}
