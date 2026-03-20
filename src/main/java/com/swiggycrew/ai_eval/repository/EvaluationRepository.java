package com.swiggycrew.ai_eval.repository;

import com.swiggycrew.ai_eval.domain.Evaluation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByConversationConversationId(String conversationId);

    @EntityGraph(attributePaths = {"suggestions"})
    List<Evaluation> findWithSuggestionsByConversationConversationId(String conversationId);
}
