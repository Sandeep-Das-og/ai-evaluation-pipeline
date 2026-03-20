package com.swiggycrew.ai_eval.model;

public class BatchEvaluationFailure {
    private String conversationId;
    private String error;

    public BatchEvaluationFailure() {}

    public BatchEvaluationFailure(String conversationId, String error) {
        this.conversationId = conversationId;
        this.error = error;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
