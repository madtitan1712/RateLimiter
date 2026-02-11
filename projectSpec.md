# 📍 Scope for v1 (DEV PHASE)

To avoid chaos, we **intentionally limit scope**:

### Included ✅

- single endpoint
- in-memory state
- rule-based limiting
- clean event logging
- AI-ready data collection

### Excluded ❌ (for now)

- dashboard UI
- Redis
- real ML model
- multi-node support

---

# 🧱 PHASE-WISE EXECUTION PLAN (FINAL)

---

## 🟢 PHASE 0 — Ground Rules (NOW)

Before writing more code:

### Design rules

- **No logic in controllers**
- **All decisions in filter**
- **State per (IP + endpoint)**
- **AI is async and optional**

This prevents refactors later.

---

## 🟢 PHASE 1 — Request Interception Layer

### What you build

- Spring `Filter` or `HandlerInterceptor`

### Responsibilities

- extract request metadata
- call rate-limit decision engine
- short-circuit if blocked

### Data extracted

```java
ip
endpoint
httpMethod
timestamp
```

No business logic here.

---

## 🟢 PHASE 2 — In-Memory State Management

### What you already have (keep it)

```java
ConcurrentHashMap<String, RateLimitState>
```

### `RateLimitState` responsibilities

- maintain deque of timestamps
- clean old entries
- expose window counts

❌ No AI

❌ No logging

❌ No dashboard logic

---

## 🟢 PHASE 3 — Rule-Based Rate Limiting (MANDATORY)

### Why

AI cannot protect you on day one.

### For v1 (single endpoint)

```
10 requests per 10 seconds per IP
```

### Decision outcomes

- ALLOW
- BLOCK

This logic is **deterministic and fast**.

---

## 🟢 PHASE 4 — Request Event Collection (CRITICAL)

This is what makes AI possible later.

### Define once — never change lightly

```java
classRequestEvent {
    String ip;
    String endpoint;
    String method;long timestamp;int responseStatus;boolean blocked;long responseTimeMs;
}
```

### Important

- This is **NOT** the deque
- This is **NOT** state
- This is a **fact log**

---

## 🟢 PHASE 5 — Event Publisher (Queue-Ready)

### For now

- in-memory queue OR
- file logging (JSON lines)

### Later

- Kafka / RabbitMQ

### Why this matters

- replayable training data
- zero coupling with request thread
- no data loss

---

## 🟢 PHASE 6 — Feature Engineering Layer (NO ML YET)

### Inputs

- deque stats
- recent request events

### Outputs (examples)

```json
{"rps_1s":6,"rps_10s":21,"avg_gap_ms":140,"endpoint":"/login","method":"POST"}
```

These get logged, not used yet.

---

## 🟢 PHASE 7 — Configuration Model (Dashboard-Ready)

Even without UI, **design for it now**.

```java
classEndpointPolicy {
    String endpoint;int maxRequests;long windowMs;boolean aiEnabled;
}
```

### For v1

- hardcode this
- single endpoint only

### For v2

- editable via dashboard

---

## 🟢 PHASE 8 — AI Integration (LATER)

### What AI will do

- consume feature logs
- learn normal vs abusive patterns
- produce `risk_score`

### What AI will NOT do

- directly block requests
- run in request thread

---

## 🟢 PHASE 9 — Decision Engine (FINAL FORM)

```java
if (ruleLimitExceeded) BLOCKelseif (riskScore > threshold) THROTTLE / BLOCKelse ALLOW
```