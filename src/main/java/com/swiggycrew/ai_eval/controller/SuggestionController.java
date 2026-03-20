package com.swiggycrew.ai_eval.controller;

import com.swiggycrew.ai_eval.domain.Evaluation;
import com.swiggycrew.ai_eval.model.EvaluationResponse;
import com.swiggycrew.ai_eval.service.EvaluationMapper;
import com.swiggycrew.ai_eval.service.EvaluationQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suggestions")
public class SuggestionController {
    private final EvaluationQueryService evaluationQueryService;

    public SuggestionController(EvaluationQueryService evaluationQueryService) {
        this.evaluationQueryService = evaluationQueryService;
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<List<EvaluationResponse.SuggestionDto>> getSuggestions(@PathVariable String conversationId) {
        Evaluation latest = evaluationQueryService.getLatest(conversationId);
        if (latest == null) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(EvaluationMapper.toResponse(latest).getImprovementSuggestions());
    }
}
