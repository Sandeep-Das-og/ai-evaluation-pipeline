package com.swiggycrew.ai_eval.service.eval;

import com.swiggycrew.ai_eval.domain.Conversation;

public interface Evaluator {
    EvaluatorResult evaluate(Conversation conversation);
}
