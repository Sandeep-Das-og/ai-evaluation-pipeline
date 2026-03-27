package com.sd.ai.eval.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ConversationIngestRequest {
    @NotBlank
    private String conversationId;

    @NotBlank
    private String agentVersion;

    @NotEmpty
    @Valid
    private List<TurnDto> turns;

    @Valid
    private FeedbackDto feedback;

    @Valid
    private MetadataDto metadata;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public List<TurnDto> getTurns() {
        return turns;
    }

    public void setTurns(List<TurnDto> turns) {
        this.turns = turns;
    }

    public FeedbackDto getFeedback() {
        return feedback;
    }

    public void setFeedback(FeedbackDto feedback) {
        this.feedback = feedback;
    }

    public MetadataDto getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataDto metadata) {
        this.metadata = metadata;
    }
}
