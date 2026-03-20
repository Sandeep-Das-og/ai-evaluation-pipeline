package com.swiggycrew.ai_eval.service.eval;

import com.swiggycrew.ai_eval.domain.Conversation;
import com.swiggycrew.ai_eval.domain.IssueDetected;
import com.swiggycrew.ai_eval.domain.IssueSeverity;
import com.swiggycrew.ai_eval.domain.ToolCall;
import com.swiggycrew.ai_eval.domain.Turn;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class ToolCallEvaluator implements Evaluator {
    @Override
    public EvaluatorResult evaluate(Conversation conversation) {
        EvaluatorResult result = new EvaluatorResult();
        double selectionAccuracy = 1.0;
        double parameterAccuracy = 1.0;
        boolean executionSuccess = true;

        List<Turn> turns = conversation.getTurns();
        for (Turn turn : turns) {
            if (turn.getToolCalls() == null || turn.getToolCalls().isEmpty()) {
                continue;
            }
            for (ToolCall toolCall : turn.getToolCalls()) {
                String toolName = toolCall.getToolName() == null ? "" : toolCall.getToolName().toLowerCase(Locale.ROOT);
                if (toolName.isBlank()) {
                    selectionAccuracy -= 0.2;
                    addIssue(result, "tool_selection", IssueSeverity.ERROR, "Missing tool name in tool call.");
                }
                if (toolCall.getParametersJson() == null || toolCall.getParametersJson().contains("null")) {
                    parameterAccuracy -= 0.2;
                    addIssue(result, "tool_parameters", IssueSeverity.WARNING, "Tool parameters missing or contain null values.");
                }
                if ("flight_search".equals(toolName) && (toolCall.getParametersJson() == null || !toolCall.getParametersJson().contains("date_range"))) {
                    parameterAccuracy -= 0.2;
                    addIssue(result, "tool_parameters", IssueSeverity.WARNING, "flight_search missing date_range parameter.");
                }
                if (Boolean.FALSE.equals(toolCall.getSuccess())) {
                    executionSuccess = false;
                    addIssue(result, "tool_execution", IssueSeverity.ERROR, "Tool execution failed.");
                }
            }
        }

        selectionAccuracy = clamp(selectionAccuracy);
        parameterAccuracy = clamp(parameterAccuracy);
        result.setSelectionAccuracy(selectionAccuracy);
        result.setParameterAccuracy(parameterAccuracy);
        result.setExecutionSuccess(executionSuccess);
        result.setToolAccuracyScore(clamp((selectionAccuracy + parameterAccuracy + (executionSuccess ? 1 : 0)) / 3.0));
        return result;
    }

    private void addIssue(EvaluatorResult result, String type, IssueSeverity severity, String description) {
        IssueDetected issue = new IssueDetected();
        issue.setType(type);
        issue.setSeverity(severity);
        issue.setDescription(description);
        result.getIssues().add(issue);
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
