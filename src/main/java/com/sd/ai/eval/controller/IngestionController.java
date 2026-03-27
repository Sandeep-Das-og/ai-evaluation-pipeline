package com.sd.ai.eval.controller;

import com.sd.ai.eval.model.ConversationIngestRequest;
import com.sd.ai.eval.model.IngestionResponse;
import com.sd.ai.eval.service.IngestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conversations")
public class IngestionController {
    private final IngestionService ingestionService;

    public IngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping
    public ResponseEntity<IngestionResponse> ingest(@Valid @RequestBody ConversationIngestRequest request) {
        boolean queued = ingestionService.ingest(request);
        String status = queued ? "QUEUED" : "PROCESSED";
        HttpStatus httpStatus = queued ? HttpStatus.ACCEPTED : HttpStatus.OK;
        return ResponseEntity.status(httpStatus)
                .body(new IngestionResponse(request.getConversationId(), status));
    }
}
