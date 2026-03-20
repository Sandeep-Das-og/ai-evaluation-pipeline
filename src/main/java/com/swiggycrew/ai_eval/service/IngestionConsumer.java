package com.swiggycrew.ai_eval.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggycrew.ai_eval.model.ConversationIngestRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class IngestionConsumer {
    private final IngestionService ingestionService;
    private final ObjectMapper objectMapper;

    public IngestionConsumer(IngestionService ingestionService, ObjectMapper objectMapper) {
        this.ingestionService = ingestionService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "conversation-ingest", groupId = "ai-eval-pipeline")
    public void handleMessage(String payload) {
        try {
            ConversationIngestRequest request = objectMapper.readValue(payload, ConversationIngestRequest.class);
            ingestionService.process(request);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to process ingestion message", e);
        }
    }
}
