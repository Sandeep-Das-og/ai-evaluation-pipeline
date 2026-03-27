package com.sd.ai.eval.controller;

import com.sd.ai.eval.domain.Evaluation;
import com.sd.ai.eval.model.EvaluationResponse;
import com.sd.ai.eval.service.EvaluationMapper;
import com.sd.ai.eval.service.EvaluationQueryService;
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
