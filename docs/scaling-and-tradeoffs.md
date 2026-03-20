# Scaling Strategy & Trade-offs

## Scaling Strategy
- **Ingestion**: Kafka enables horizontal scaling of consumers (partitioned topic)
- **Evaluation**: Stateless evaluator services can scale out independently
- **Storage**: Partition conversation tables by time; use read replicas for analytics
- **Caching**: Redis for repeated evaluations and hot metadata
- **Async Jobs**: Backfill/reevaluation via Kafka replay or batch jobs

## Handling 10x / 100x Load
- Increase Kafka partitions and consumers to parallelize evaluations
- Add autoscaling based on lag and evaluation latency
- Adopt async evaluation for heavy LLM-based checks
- Use columnar storage (e.g., ClickHouse) for analytics queries

## Trade-offs
- **LLM-as-Judge stub**: no API costs, but lower fidelity; easy to swap later
- **JPA relational model**: fast iteration but can be replaced with event store at scale
- **Simple suggestion rules**: pragmatic for prototype; future ML models can learn patterns

## With More Time
- Add evaluator accuracy tracking (precision/recall vs human labels)
- Implement disagreement routing and human review queue
- Add prompt/tool version management and automatic rollbacks
