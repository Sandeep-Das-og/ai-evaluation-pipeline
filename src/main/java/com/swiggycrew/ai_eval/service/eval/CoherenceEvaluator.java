package com.swiggycrew.ai_eval.service.eval;

import com.swiggycrew.ai_eval.domain.Conversation;
import com.swiggycrew.ai_eval.domain.IssueDetected;
import com.swiggycrew.ai_eval.domain.IssueSeverity;
import com.swiggycrew.ai_eval.domain.Turn;
import org.springframework.stereotype.Component;

@Component
public class CoherenceEvaluator implements Evaluator {
    @Override
    public EvaluatorResult evaluate(Conversation conversation) {
        EvaluatorResult result = new EvaluatorResult();
        int turns = conversation.getTurns().size();
        double score = 1.0;
        if (turns > 5) {
            score -= 0.1;
        }

        boolean contradiction = false;
        boolean positive = false;
        boolean negative = false;
        for (Turn turn : conversation.getTurns()) {
            if (!"ASSISTANT".equals(turn.getRole().name())) {
                continue;
            }
            String content = turn.getContent().toLowerCase();
            if (content.contains("sure") || content.contains("happy to help") || content.contains("i can")) {
                positive = true;
            }
            if (content.contains("can't") || content.contains("cannot") || content.contains("unable")) {
                negative = true;
            }
            if (positive && negative) {
                contradiction = true;
                break;
            }
        }

        if (contradiction) {
            score -= 0.3;
            IssueDetected issue = new IssueDetected();
            issue.setType("coherence");
            issue.setSeverity(IssueSeverity.WARNING);
            issue.setDescription("Potential contradiction across assistant turns.");
            result.getIssues().add(issue);
        }

        result.setCoherenceScore(clamp(score));
        return result;
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
