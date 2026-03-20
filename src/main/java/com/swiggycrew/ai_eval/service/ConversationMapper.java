package com.swiggycrew.ai_eval.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggycrew.ai_eval.domain.*;
import com.swiggycrew.ai_eval.model.*;
import org.springframework.stereotype.Component;

@Component
public class ConversationMapper {
    private final ObjectMapper objectMapper;

    public ConversationMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Conversation toEntity(ConversationIngestRequest request) {
        Conversation conversation = new Conversation();
        conversation.setConversationId(request.getConversationId());
        conversation.setAgentVersion(request.getAgentVersion());
        if (request.getMetadata() != null) {
            conversation.setTotalLatencyMs(request.getMetadata().getTotalLatencyMs());
            conversation.setMissionCompleted(request.getMetadata().getMissionCompleted());
        }

        if (request.getTurns() != null) {
            for (TurnDto turnDto : request.getTurns()) {
                Turn turn = new Turn();
                turn.setTurnId(turnDto.getTurnId());
                turn.setRole(Role.valueOf(turnDto.getRole().toUpperCase()));
                turn.setContent(turnDto.getContent());
                turn.setTimestamp(turnDto.getTimestamp());
                turn.setConversation(conversation);

                if (turnDto.getToolCalls() != null) {
                    for (ToolCallDto toolCallDto : turnDto.getToolCalls()) {
                        ToolCall toolCall = new ToolCall();
                        toolCall.setToolName(toolCallDto.getToolName());
                        toolCall.setParametersJson(writeJson(toolCallDto.getParameters()));
                        toolCall.setResultJson(writeJson(toolCallDto.getResult()));
                        toolCall.setLatencyMs(toolCallDto.getLatencyMs());
                        toolCall.setSuccess(extractSuccess(toolCallDto));
                        toolCall.setTurn(turn);
                        turn.getToolCalls().add(toolCall);
                    }
                }

                conversation.getTurns().add(turn);
            }
        }

        if (request.getFeedback() != null) {
            Feedback feedback = new Feedback();
            feedback.setUserRating(request.getFeedback().getUserRating());
            feedback.setRephrasingCount(request.getFeedback().getRephrasingCount());
            feedback.setEarlyExit(request.getFeedback().getEarlyExit());
            if (request.getFeedback().getOpsReview() != null) {
                feedback.setOpsQuality(request.getFeedback().getOpsReview().getQuality());
                feedback.setOpsNotes(request.getFeedback().getOpsReview().getNotes());
            }
            feedback.setConversation(conversation);

            if (request.getFeedback().getAnnotations() != null) {
                for (AnnotationDto annotationDto : request.getFeedback().getAnnotations()) {
                    Annotation annotation = new Annotation();
                    annotation.setType(annotationDto.getType());
                    annotation.setLabel(annotationDto.getLabel());
                    annotation.setAnnotatorId(annotationDto.getAnnotatorId());
                    annotation.setConfidence(annotationDto.getConfidence());
                    annotation.setFeedback(feedback);
                    feedback.getAnnotations().add(annotation);
                }
            }

            conversation.setFeedback(feedback);
        }

        return conversation;
    }

    private String writeJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize JSON payload", e);
        }
    }

    private boolean extractSuccess(ToolCallDto toolCallDto) {
        if (toolCallDto.getResult() == null) {
            return false;
        }
        Object status = toolCallDto.getResult().get("status");
        if (status == null) {
            return false;
        }
        return "success".equalsIgnoreCase(String.valueOf(status));
    }
}
