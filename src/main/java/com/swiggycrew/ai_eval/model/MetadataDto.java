package com.swiggycrew.ai_eval.model;

public class MetadataDto {
    private Long totalLatencyMs;
    private Boolean missionCompleted;

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
}
