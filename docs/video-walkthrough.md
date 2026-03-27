## Video Walkthrough (5–10 min)

### Goal
Show the architecture, demo core flows, and explain what you’d do with more time.

## Spoken Script (7–10 min)

### 1) Intro (30s)
“Hi, I’m going to walk through the AI Agent Evaluation Pipeline.  
This system ingests multi‑turn conversations, evaluates them using modular checks, stores issues and suggestions, and surfaces improvement opportunities for prompts and tools.”

### 2) Why This Stack (1–2 min)
“I chose Spring Boot with Java 21 because it’s fast to build stable APIs and has a strong ecosystem for observability and production readiness.  
PostgreSQL is a natural fit for structured conversation, feedback, and evaluation data.  
Kafka decouples ingestion from evaluation so the system can handle high‑throughput spikes and replay when needed.  
Redis gives quick access to the latest evaluations.  
Finally, Render provides the fastest path to a live hosted demo for reviewers.”

### 3) Architecture & Key Decisions (2–3 min)
“Here in `docs/architecture.md`, you can see the high‑level design.  
Ingestion accepts conversations and either publishes to Kafka for real‑time processing or processes directly.  
The evaluation engine is modular: there’s a stubbed LLM‑as‑Judge, tool call evaluator, multi‑turn coherence evaluator, and heuristic checks like latency.  
Results are persisted in PostgreSQL, and the latest evaluation is cached in Redis.  
In `docs/scaling-and-tradeoffs.md`, I discuss scaling paths, including Kafka partitioning, stateless evaluation workers, and replay for backfills.”

### 4) Live Demo (3–4 min)
“Now I’ll open the hosted UI at the Render URL.  
First, I’ll ingest a sample conversation.  
Next, I’ll run an evaluation and show the returned scores, detected issues, and improvement suggestions.  
Notice the routing decision field — it shows whether this evaluation can be auto‑accepted or should be routed for human review based on annotator agreement and confidence.  
I’ll also fetch the latest evaluation and the latest suggestions to show the query endpoints.  
Optionally, I can run a batch evaluation for multiple conversation IDs.  
If needed, the Swagger UI is available for the full API surface.”

### 5) What I’d Do With More Time (1–2 min)
“I’d invest in meta‑evaluation — calibrating the LLM‑as‑Judge against human labels and tracking precision/recall over time.  
I’d build stronger disagreement routing: a tie‑breaker review queue and annotator trust scoring.  
I’d add true batch pipelines for scheduled re‑evaluation, historical backfills, and drift detection.  
I’d implement regression alerts based on baselines so teams are notified when key scores dip.  
And I’d expand tool‑schema feedback to auto‑propose validation rules and schema changes.”

### Demo Checklist (for your reference)
- [ ] Hosted UI loads
- [ ] Ingest conversation succeeds
- [ ] Run evaluation returns scores
- [ ] Latest evaluation and suggestions work
- [ ] Show routing decision and annotation agreement
- [ ] Show architecture docs
- [ ] Mention scaling & trade‑offs

### Recording Tips
- Use screen recording + mic.
- Keep demo on one tab; copy/paste IDs to avoid typos.
- End with “next steps / improvements.”
