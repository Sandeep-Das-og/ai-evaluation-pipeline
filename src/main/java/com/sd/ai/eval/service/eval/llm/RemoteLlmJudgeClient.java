package com.sd.ai.eval.service.eval.llm;

import com.sd.ai.eval.domain.Conversation;
import com.sd.ai.eval.domain.Turn;
import com.sd.ai.eval.service.eval.llm.dto.LlmJudgeRequest;
import com.sd.ai.eval.service.eval.llm.dto.LlmJudgeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "app.eval.llm-judge.mode", havingValue = "remote")
public class RemoteLlmJudgeClient implements LlmJudgeClient {
    private final WebClient webClient;
    private final String endpoint;
    private final String apiKey;
    private final long timeoutMs;

    public RemoteLlmJudgeClient(WebClient.Builder webClientBuilder,
                                @Value("${app.eval.llm-judge.endpoint}") String endpoint,
                                @Value("${app.eval.llm-judge.api-key:}") String apiKey,
                                @Value("${app.eval.llm-judge.timeout-ms:1500}") long timeoutMs) {
        this.webClient = webClientBuilder.build();
        this.endpoint = endpoint;
        this.apiKey = apiKey;
        this.timeoutMs = timeoutMs;
    }

    @Override
    public double score(Conversation conversation) {
        LlmJudgeRequest request = new LlmJudgeRequest();
        request.setConversationId(conversation.getConversationId());
        request.setAgentVersion(conversation.getAgentVersion());
        request.setTurns(mapTurns(conversation.getTurns()));

        LlmJudgeResponse response = webClient.post()
                .uri(endpoint)
                .header(HttpHeaders.AUTHORIZATION, apiKey.isBlank() ? "" : "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LlmJudgeResponse.class)
                .timeout(Duration.ofMillis(timeoutMs))
                .block();

        if (response == null || response.getScore() == null) {
            throw new IllegalStateException("LLM judge returned no score");
        }
        return clamp(response.getScore());
    }

    private List<LlmJudgeRequest.TurnPayload> mapTurns(List<Turn> turns) {
        return turns.stream().map(turn -> {
            LlmJudgeRequest.TurnPayload payload = new LlmJudgeRequest.TurnPayload();
            payload.setTurnId(turn.getTurnId());
            payload.setRole(turn.getRole().name().toLowerCase());
            payload.setContent(turn.getContent());
            return payload;
        }).collect(Collectors.toList());
    }

    private double clamp(double value) {
        if (value < 0) {
            return 0;
        }
        if (value > 1) {
            return 1;
        }
        return value;
    }
}
