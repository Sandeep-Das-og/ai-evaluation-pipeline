package com.sd.ai.eval.model;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public class ToolCallDto {
    @NotBlank
    private String toolName;

    private Map<String, Object> parameters;

    private Map<String, Object> result;

    private Long latencyMs;

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public Long getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(Long latencyMs) {
        this.latencyMs = latencyMs;
    }
}
