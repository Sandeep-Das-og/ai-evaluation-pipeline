package com.swiggycrew.ai_eval.service.eval.llm.dto;

import com.swiggycrew.ai_eval.domain.Turn;
import java.util.List;

public class LlmJudgeRequest {
    private String conversationId;
    private String agentVersion;
    private List<TurnPayload> turns;

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

    public List<TurnPayload> getTurns() {
        return turns;
    }

    public void setTurns(List<TurnPayload> turns) {
        this.turns = turns;
    }

    public static class TurnPayload {
        private Integer turnId;
        private String role;
        private String content;

        public Integer getTurnId() {
            return turnId;
        }

        public void setTurnId(Integer turnId) {
            this.turnId = turnId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
