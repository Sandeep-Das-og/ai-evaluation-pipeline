package com.swiggycrew.ai_eval.service.eval;

import com.swiggycrew.ai_eval.domain.Conversation;
import com.swiggycrew.ai_eval.service.eval.llm.LlmJudgeClient;
import org.springframework.stereotype.Component;

@Component
public class LlmJudgeEvaluator implements Evaluator {
    private final LlmJudgeClient llmJudgeClient;

    public LlmJudgeEvaluator(LlmJudgeClient llmJudgeClient) {
        this.llmJudgeClient = llmJudgeClient;
    }

    @Override
    public EvaluatorResult evaluate(Conversation conversation) {
        EvaluatorResult result = new EvaluatorResult();
        double score = llmJudgeClient.score(conversation);
        result.setResponseQualityScore(clamp(score));
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
