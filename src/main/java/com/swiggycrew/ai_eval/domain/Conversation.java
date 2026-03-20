package com.swiggycrew.ai_eval.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String conversationId;

    private String agentVersion;

    private Long totalLatencyMs;

    private Boolean missionCompleted;

    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("turnId ASC")
    private List<Turn> turns = new ArrayList<>();

    @OneToOne(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Feedback feedback;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Evaluation> evaluations = new ArrayList<>();

    public Long getId() {
        return id;
    }

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

    public Long getTotalLatencyMs() {
        return totalLatencyMs;
    }

    public void setTotalLatencyMs(Long totalLatencyMs) {
        this.totalLatencyMs = totalLatencyMs;
    }

    public Boolean getMissionCompleted() {
        return missionCompleted;
    }

    public void setMissionCompleted(Boolean missionCompleted) {
        this.missionCompleted = missionCompleted;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }
}
