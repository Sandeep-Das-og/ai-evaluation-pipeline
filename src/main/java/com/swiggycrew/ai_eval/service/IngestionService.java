package com.swiggycrew.ai_eval.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggycrew.ai_eval.domain.Conversation;
import com.swiggycrew.ai_eval.model.ConversationIngestRequest;
import com.swiggycrew.ai_eval.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IngestionService {
    private final ConversationMapper mapper;
    private final ConversationRepository conversationRepository;
    private final EvaluationService evaluationService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final boolean realtimeEnabled;

    public IngestionService(ConversationMapper mapper,
                            ConversationRepository conversationRepository,
                            EvaluationService evaluationService,
                            KafkaTemplate<String, String> kafkaTemplate,
                            ObjectMapper objectMapper,
                            @Value("${app.ingestion.realtime-enabled:true}") boolean realtimeEnabled) {
        this.mapper = mapper;
        this.conversationRepository = conversationRepository;
        this.evaluationService = evaluationService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.realtimeEnabled = realtimeEnabled;
    }

    public boolean ingest(ConversationIngestRequest request) {
        if (realtimeEnabled) {
            try {
                String payload = objectMapper.writeValueAsString(request);
                kafkaTemplate.send("conversation-ingest", request.getConversationId(), payload);
                return true;
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Failed to serialize ingestion payload", e);
            } catch (Exception ex) {
                // fall back to direct processing
            }
        }
        process(request);
        return false;
    }

    @Transactional
    public void process(ConversationIngestRequest request) {
        Conversation conversation = mapper.toEntity(request);
        Conversation saved = conversationRepository.save(conversation);
        evaluationService.evaluate(saved);
    }
}
