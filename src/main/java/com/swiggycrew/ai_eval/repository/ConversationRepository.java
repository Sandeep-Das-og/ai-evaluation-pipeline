package com.swiggycrew.ai_eval.repository;

import com.swiggycrew.ai_eval.domain.Conversation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByConversationId(String conversationId);

    @EntityGraph(attributePaths = {"turns"})
    Optional<Conversation> findWithTurnsByConversationId(String conversationId);
}
