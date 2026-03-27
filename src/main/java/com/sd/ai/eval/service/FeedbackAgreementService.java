package com.sd.ai.eval.service;

import com.sd.ai.eval.domain.Annotation;
import com.sd.ai.eval.domain.Feedback;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedbackAgreementService {
    public Double computeAgreement(Feedback feedback) {
        if (feedback == null || feedback.getAnnotations() == null || feedback.getAnnotations().isEmpty()) {
            return null;
        }
        Map<String, Integer> labelCounts = new HashMap<>();
        List<Annotation> annotations = feedback.getAnnotations();
        for (Annotation annotation : annotations) {
            String key = annotation.getType() + "::" + annotation.getLabel();
            labelCounts.put(key, labelCounts.getOrDefault(key, 0) + 1);
        }
        int max = labelCounts.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        return Math.round((max / (double) annotations.size()) * 100.0) / 100.0;
    }

    public Double computeAverageConfidence(Feedback feedback) {
        if (feedback == null || feedback.getAnnotations() == null || feedback.getAnnotations().isEmpty()) {
            return null;
        }
        List<Annotation> annotations = feedback.getAnnotations();
        double total = 0;
        int count = 0;
        for (Annotation annotation : annotations) {
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
