package com.sd.ai.eval.model;

import java.util.List;

public class BatchEvaluationRequest {
    private List<String> conversationIds;
    private boolean force;

    public List<String> getConversationIds() {
        return conversationIds;
    }

    public void setConversationIds(List<String> conversationIds) {
        this.conversationIds = conversationIds;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }
}
