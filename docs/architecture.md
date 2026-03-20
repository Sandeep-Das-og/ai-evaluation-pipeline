# Architecture Decisions

## Goals
- Detect regressions before users are impacted
- Align evaluation scores with user feedback
- Surface improvement opportunities for prompts and tools
- Generate actionable suggestions automatically
- Scale to production throughput

## High-Level Design
- **API Layer**: REST ingestion and query endpoints (Spring MVC)
- **Streaming Layer**: Kafka for high-throughput, low-latency ingest and replay
- **Evaluation Engine**: Pluggable evaluators
  - LLM-as-Judge (stubbed, swappable)
  - Tool Call Evaluator
  - Multi-turn Coherence Evaluator
  - Heuristic checks (latency, required fields)
- **Persistence**: PostgreSQL for conversations, feedback, evaluations, and suggestions
- **Suggestions**: Rule-based generator tied to issue patterns

## Why This Design
- Kafka decouples ingestion from evaluation to smooth burst traffic
- JPA entities keep the prototype simple while allowing analytics
- Evaluator interfaces enable quick iteration and future LLM integration
- Structured suggestions make prompt/tool improvements operationally actionable

## Data Flow
1. Conversation payload arrives via `/api/v1/conversations`
2. If real-time enabled, request is published to Kafka (`conversation-ingest` topic)
3. Consumer processes payload, persists conversation and feedback
4. Evaluation engine runs modular evaluators
5. Issues and suggestions stored and queryable

## Reliability
- Kafka provides at-least-once delivery and replay for reprocessing
- Validation ensures required fields on ingestion
- Evaluation is idempotent at the conversation level
