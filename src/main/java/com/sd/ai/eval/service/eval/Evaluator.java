package com.sd.ai.eval.service.eval;

import com.sd.ai.eval.domain.Conversation;

public interface Evaluator {
    EvaluatorResult evaluate(Conversation conversation);
}
