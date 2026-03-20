package com.swiggycrew.ai_eval.controller;

import com.swiggycrew.ai_eval.domain.Conversation;
import com.swiggycrew.ai_eval.domain.Evaluation;
import com.swiggycrew.ai_eval.model.EvaluationResponse;
import com.swiggycrew.ai_eval.repository.ConversationRepository;
import com.swiggycrew.ai_eval.repository.EvaluationRepository;
import com.swiggycrew.ai_eval.service.EvaluationMapper;
import com.swiggycrew.ai_eval.service.EvaluationQueryService;
import com.swiggycrew.ai_eval.service.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/evaluations")
public class EvaluationController {
    private final EvaluationService evaluationService;
    private final ConversationRepository conversationRepository;
    private final EvaluationRepository evaluationRepository;
    private final EvaluationQueryService evaluationQueryService;

    public EvaluationController(EvaluationService evaluationService,
                                ConversationRepository conversationRepository,
                                EvaluationRepository evaluationRepository,
                                EvaluationQueryService evaluationQueryService) {
        this.evaluationService = evaluationService;
        this.conversationRepository = conversationRepository;
        this.evaluationRepository = evaluationRepository;
        this.evaluationQueryService = evaluationQueryService;
    }

    @PostMapping("/{conversationId}")
    public ResponseEntity<EvaluationResponse> evaluateConversation(@PathVariable String conversationId,
                                                                   @RequestParam(defaultValue = "false") boolean force) {
        Evaluation evaluation = evaluationService.evaluateByConversationId(conversationId, force);
        return ResponseEntity.ok(EvaluationMapper.toResponse(evaluation));
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<List<EvaluationResponse>> getEvaluations(@PathVariable String conversationId) {
        List<Evaluation> evaluations = evaluationRepository.findByConversationConversationId(conversationId);
        return ResponseEntity.ok(evaluations.stream().map(EvaluationMapper::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{conversationId}/latest")
    public ResponseEntity<EvaluationResponse> getLatest(@PathVariable String conversationId) {
        Evaluation latest = evaluationQueryService.getLatest(conversationId);
        if (latest == null) {
            throw new IllegalArgumentException("No evaluation found");
        }
        return ResponseEntity.ok(EvaluationMapper.toResponse(latest));
    }
}
