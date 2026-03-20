# Swiggy Crew AI Agent Evaluation Pipeline

This project implements a production-style evaluation pipeline for AI agents, including ingestion, automated evaluation, feedback integration, and self-improving suggestions.

## Architecture Overview

Core pipeline:
1. **Ingestion API** accepts multi-turn conversations and feedback
2. **Kafka** buffers high-throughput streams (real-time)
3. **Evaluation Engine** runs modular evaluators (LLM-as-Judge stub, Tool Call, Coherence, Heuristics)
4. **PostgreSQL** stores conversations, feedback, evaluations, issues, and suggestions
5. **Suggestions** are generated automatically based on detected failure patterns

For design details and scaling strategy see:
- `docs/architecture.md`
- `docs/scaling-and-tradeoffs.md`

## Tech Stack
- Java 21, Spring Boot 3
- PostgreSQL, Kafka, Redis (extensible for caching)
- OpenAPI/Swagger via springdoc

## Local Setup

1. Start infrastructure:

```bash
cd docker
docker-compose up -d
```

2. Run the app:

```bash
./gradlew bootRun
```

3. Open Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

## Local Run URLs

- App API base: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Actuator metrics: `http://localhost:8080/actuator/metrics`
- Mock LLM judge: `http://localhost:8080/mock/llm-judge`
- PostgreSQL: `postgresql://localhost:5432/aieval`
- Kafka broker: `localhost:9092`
- Zookeeper: `localhost:2181`
- Redis: `localhost:6379`

## API Endpoints

- `POST /api/v1/conversations` — ingest conversation
- `POST /api/v1/evaluations/{conversationId}` — re-run evaluation (`force=true` bypasses cache)
- `GET /api/v1/evaluations/{conversationId}` — list evaluations
- `GET /api/v1/evaluations/{conversationId}/latest` — latest evaluation (cached)
- `GET /api/v1/suggestions/{conversationId}` — latest improvement suggestions

## Sample Ingestion

```bash
curl -X POST http://localhost:8080/api/v1/conversations \
  -H 'Content-Type: application/json' \
  -d @sample.json
```

Example `sample.json`:

```json
{
  "conversationId": "conv_abc123",
  "agentVersion": "v2.3.1",
  "turns": [
    {
      "turnId": 1,
      "role": "user",
      "content": "I need to book a flight to NYC next week",
      "timestamp": "2024-01-15T10:30:00Z"
    },
    {
      "turnId": 2,
      "role": "assistant",
      "content": "I'd be happy to help you book a flight to NYC.",
      "toolCalls": [
        {
          "toolName": "flight_search",
          "parameters": {
            "destination": "NYC",
            "date_range": "2024-01-22/2024-01-28"
          },
          "result": {
            "status": "success",
            "flights": ["..."]
          },
          "latencyMs": 450
        }
      ],
      "timestamp": "2024-01-15T10:30:02Z"
    }
  ],
  "feedback": {
    "userRating": 4,
    "opsReview": {
      "quality": "good",
      "notes": "Correct tool usage"
    },
    "annotations": [
      {
        "type": "tool_accuracy",
        "label": "correct",
        "annotatorId": "ann_001",
        "confidence": 0.9
      }
    ]
  },
  "metadata": {
    "totalLatencyMs": 1200,
    "missionCompleted": true
  }
}
```

## LLM Judge Modes

Default mode is `stub` to avoid external API costs. Switch to `remote` to use a real LLM judge.

```yaml
app:
  eval:
    llm-judge:
      mode: stub
      endpoint: http://localhost:9000/llm-judge
      api-key: ""
      timeout-ms: 1500
```

### Local Mock LLM Judge
A built-in mock service is available for local testing.

Enable:
```
app.eval.llm-judge.mode=remote
app.eval.llm-judge.endpoint=http://localhost:8080/mock/llm-judge
app.eval.llm-judge.mock-enabled=true
```

Endpoint:
- `POST /mock/llm-judge`

## Cache Metrics
Cache hit/miss metrics are exported via Actuator.

Metrics:
- `cache.latest_evaluation.hit`
- `cache.latest_evaluation.miss`

Actuator endpoint:
- `GET /actuator/metrics`

## Tests & Coverage

Run tests:

```bash
./gradlew test
```

Coverage reports:
- HTML: `build/reports/jacoco/test/html/index.html`
- XML: `build/reports/jacoco/test/jacocoTestReport.xml`

## CI (GitHub Actions)

Workflow:
- `.github/workflows/ci.yml`
- Runs `./gradlew test` and uploads JaCoCo HTML report as an artifact

## Notes on LLM Integration
This prototype uses a **rule-based LLM-as-Judge stub** by default. The evaluator interface is designed for easy replacement with real LLM calls when needed.

## Next Steps (Optional)
- Add model registry + prompt version tracking
- Add human review queue with confidence-based routing
- Add evaluator precision/recall tracking vs human labels
