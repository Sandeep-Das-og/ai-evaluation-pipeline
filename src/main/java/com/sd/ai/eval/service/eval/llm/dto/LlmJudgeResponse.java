package com.sd.ai.eval.service.eval.llm.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response from the mock LLM judge service.")
public class LlmJudgeResponse {
    @Schema(description = "Normalized quality score in [0,1].", example = "0.72")
    private Double score;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
