package com.sd.ai.eval;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sd.ai.eval.model.ConversationIngestRequest;
import com.sd.ai.eval.repository.ConversationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IngestionAndEvaluationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConversationRepository conversationRepository;

    @Test
    void ingestConversation_createsConversation() throws Exception {
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

        assertThat(conversationRepository.findByConversationId(request.getConversationId())).isPresent();
    }
}
