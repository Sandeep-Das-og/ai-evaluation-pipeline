package com.swiggycrew.ai_eval.service.eval.llm;

import com.swiggycrew.ai_eval.domain.Conversation;

public interface LlmJudgeClient {
    double score(Conversation conversation);
}
