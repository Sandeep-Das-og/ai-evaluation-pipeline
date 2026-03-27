package com.sd.ai.eval.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public class TurnDto {
    @NotNull
    private Integer turnId;

    @NotBlank
    private String role;

    @NotBlank
    private String content;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;

    @Valid
    private List<ToolCallDto> toolCalls;

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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public List<ToolCallDto> getToolCalls() {
        return toolCalls;
    }

    public void setToolCalls(List<ToolCallDto> toolCalls) {
        this.toolCalls = toolCalls;
    }
}
