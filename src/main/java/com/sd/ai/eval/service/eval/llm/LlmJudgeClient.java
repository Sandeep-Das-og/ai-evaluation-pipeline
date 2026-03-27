package com.sd.ai.eval.service.eval.llm;

import com.sd.ai.eval.domain.Conversation;

public interface LlmJudgeClient {
    double score(Conversation conversation);
}
