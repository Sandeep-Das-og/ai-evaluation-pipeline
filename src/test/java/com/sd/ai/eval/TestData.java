package com.sd.ai.eval;

import com.sd.ai.eval.model.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class TestData {
    private TestData() {}

    public static ConversationIngestRequest sampleConversation() {
        ConversationIngestRequest request = new ConversationIngestRequest();
        request.setConversationId("conv_" + java.util.UUID.randomUUID());
        request.setAgentVersion("v1.0.0");

        TurnDto user = new TurnDto();
        user.setTurnId(1);
        user.setRole("user");
        user.setContent("Book a flight to NYC next week");
        user.setTimestamp(Instant.parse("2024-01-15T10:30:00Z"));

        TurnDto assistant = new TurnDto();
        assistant.setTurnId(2);
        assistant.setRole("assistant");
        assistant.setContent("Sure, searching flights.");
        assistant.setTimestamp(Instant.parse("2024-01-15T10:30:02Z"));

        ToolCallDto toolCall = new ToolCallDto();
        toolCall.setToolName("flight_search");
        toolCall.setParameters(Map.of("destination", "NYC", "date_range", "2024-01-22/2024-01-28"));
        toolCall.setResult(Map.of("status", "success"));
        toolCall.setLatencyMs(450L);

        assistant.setToolCalls(List.of(toolCall));

        request.setTurns(List.of(user, assistant));

        FeedbackDto feedback = new FeedbackDto();
        feedback.setUserRating(4);

        request.setFeedback(feedback);

        MetadataDto metadata = new MetadataDto();
        metadata.setTotalLatencyMs(900L);
        metadata.setMissionCompleted(true);
        request.setMetadata(metadata);

        return request;
    }
}
