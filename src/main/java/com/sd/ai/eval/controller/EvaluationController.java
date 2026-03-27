package com.sd.ai.eval.controller;

import com.sd.ai.eval.domain.Conversation;
import com.sd.ai.eval.domain.Evaluation;
import com.sd.ai.eval.model.BatchEvaluationFailure;
import com.sd.ai.eval.model.BatchEvaluationRequest;
import com.sd.ai.eval.model.BatchEvaluationResult;
import com.sd.ai.eval.model.EvaluationResponse;
import com.sd.ai.eval.repository.ConversationRepository;
import com.sd.ai.eval.repository.EvaluationRepository;
import com.sd.ai.eval.service.EvaluationMapper;
import com.sd.ai.eval.service.EvaluationQueryService;
import com.sd.ai.eval.service.EvaluationService;
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

    @PostMapping("/batch")
    public ResponseEntity<BatchEvaluationResult> evaluateBatch(@RequestBody BatchEvaluationRequest request) {
        BatchEvaluationResult result = new BatchEvaluationResult();
        if (request.getConversationIds() == null || request.getConversationIds().isEmpty()) {
            return ResponseEntity.ok(result);
        }
        for (String conversationId : request.getConversationIds()) {
            try {
                Evaluation evaluation = evaluationService.evaluateByConversationId(conversationId, request.isForce());
                result.getEvaluations().add(EvaluationMapper.toResponse(evaluation));
            } catch (Exception ex) {
                result.getFailures().add(new BatchEvaluationFailure(conversationId, ex.getMessage()));
            }
        }
        return ResponseEntity.ok(result);
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
