package com.sd.ai.eval.service.eval;

import com.sd.ai.eval.domain.Conversation;
import com.sd.ai.eval.domain.IssueDetected;
import com.sd.ai.eval.domain.IssueSeverity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HeuristicEvaluator implements Evaluator {
    private final long latencyThresholdMs;

    public HeuristicEvaluator(@Value("${app.eval.latency-threshold-ms:1000}") long latencyThresholdMs) {
        this.latencyThresholdMs = latencyThresholdMs;
    }

    @Override
    public EvaluatorResult evaluate(Conversation conversation) {
        EvaluatorResult result = new EvaluatorResult();
        if (conversation.getTotalLatencyMs() != null && conversation.getTotalLatencyMs() > latencyThresholdMs) {
            IssueDetected issue = new IssueDetected();
            issue.setType("latency");
            issue.setSeverity(IssueSeverity.WARNING);
            issue.setDescription("Response latency " + conversation.getTotalLatencyMs() + "ms exceeds " + latencyThresholdMs + "ms target");
            result.getIssues().add(issue);
        }
        return result;
    }
}
