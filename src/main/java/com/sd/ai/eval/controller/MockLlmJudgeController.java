package com.sd.ai.eval.controller;

import com.sd.ai.eval.service.eval.llm.dto.LlmJudgeRequest;
import com.sd.ai.eval.service.eval.llm.dto.LlmJudgeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mock/llm-judge")
@ConditionalOnProperty(name = "app.eval.llm-judge.mock-enabled", havingValue = "true")
public class MockLlmJudgeController {
    @Operation(
            summary = "Mock LLM Judge",
            description = "Local mock LLM judge endpoint for testing. Returns a normalized score.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Mock judge score",
                            content = @Content(schema = @Schema(implementation = LlmJudgeResponse.class))
                    )
            }
    )
    @PostMapping
    public LlmJudgeResponse judge(@RequestBody LlmJudgeRequest request) {
        LlmJudgeResponse response = new LlmJudgeResponse();
        double lengthScore = request.getTurns() == null ? 0.5 : Math.min(1.0, request.getTurns().size() / 6.0);
        response.setScore(Math.max(0.2, Math.min(1.0, lengthScore)));
        return response;
    }
}
