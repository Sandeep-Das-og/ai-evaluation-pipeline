package com.sd.ai.eval.service;

import com.sd.ai.eval.domain.Evaluation;
import com.sd.ai.eval.repository.EvaluationRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class EvaluationQueryService {
    private final EvaluationRepository evaluationRepository;
    private final EvaluationCacheService cacheService;

    public EvaluationQueryService(EvaluationRepository evaluationRepository, EvaluationCacheService cacheService) {
        this.evaluationRepository = evaluationRepository;
        this.cacheService = cacheService;
    }

    @Transactional(readOnly = true)
    public Evaluation getLatest(String conversationId) {
        Evaluation cached = cacheService.getLatest(conversationId);
        if (cached != null) {
            return cached;
        }
        List<Evaluation> evaluations = evaluationRepository.findByConversationConversationId(conversationId);
        if (evaluations.isEmpty()) {
            return null;
        }
        Evaluation latest = evaluations.stream().max(Comparator.comparing(Evaluation::getCreatedAt)).orElse(null);
        if (latest != null) {
            Hibernate.initialize(latest.getIssues());
            Hibernate.initialize(latest.getSuggestions());
            cacheService.putLatest(conversationId, latest);
        }
        return latest;
    }
}
