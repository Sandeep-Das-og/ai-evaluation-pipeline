package com.sd.ai.eval.model;

public class IngestionResponse {
    private String conversationId;
    private String status;

    public IngestionResponse() {}

    public IngestionResponse(String conversationId, String status) {
        this.conversationId = conversationId;
        this.status = status;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
