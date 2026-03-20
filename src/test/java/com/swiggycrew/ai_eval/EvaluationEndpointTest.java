package com.swiggycrew.ai_eval;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggycrew.ai_eval.model.ConversationIngestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EvaluationEndpointTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void evaluateConversation_returnsScores() throws Exception {
        ConversationIngestRequest request = TestData.sampleConversation();

        mockMvc.perform(post("/api/v1/conversations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andDo(result -> {
                    if (result.getResponse().getStatus() < 200 || result.getResponse().getStatus() >= 300) {
                        System.out.println("Ingest error body: " + result.getResponse().getContentAsString());
                    }
                });

        mockMvc.perform(post("/api/v1/evaluations/" + request.getConversationId())
                        .param("force", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scores.overall").exists())
                .andExpect(jsonPath("$.scores.responseQuality").exists());
    }
}
