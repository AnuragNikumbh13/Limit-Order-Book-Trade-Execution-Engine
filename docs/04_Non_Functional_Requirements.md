# Non-Functional Requirements

## 1. Introduction

This document defines the non-functional requirements for the
Limit Order Book & Trade Execution Engine.

These requirements describe the expected quality attributes of
the system, including security, performance, reliability,
maintainability, and data consistency.

---

## 2. Performance

### NFR-001 — Request Processing

The system shall process order-placement requests synchronously
through the REST API and matching engine.

### NFR-002 — Load Testing

The application shall be evaluated under load using Apache JMeter.

A benchmark was performed with:

- 200 concurrent threads
- 250 iterations per thread
- 50,000 total HTTP requests
- 0% request errors

Observed result in the local development environment:

```text
Total Requests : 50,000
Errors         : 0
Throughput     : 112.3 requests/second
Average Time   : 1,727 ms
Maximum Time   : 5,608 ms
```

These results represent the observed performance on the local
development environment and are not intended to represent
production-scale trading system performance.

### NFR-003 — Performance Measurement

Performance measurements shall be based on actual benchmark
results rather than assumed throughput targets.

---

## 3. Security

### NFR-004 — Authentication

Protected business APIs shall require successful authentication.

### NFR-005 — JWT Authentication

The system shall use JWT-based stateless authentication for
protected requests.

### NFR-006 — Password Security

User passwords shall be stored using BCrypt hashing and shall
never be persisted as plain text.

### NFR-007 — Authorization

Unauthenticated requests shall not be allowed to access
protected business endpoints.

### NFR-008 — Sensitive Data Protection

Sensitive information such as passwords, JWT tokens, and
database credentials shall not be exposed through application
logs or committed to source control.

---

## 4. Reliability

### NFR-009 — Transactional Consistency

Order and trade database updates shall maintain transactional
consistency.

### NFR-010 — Rollback

If an unchecked failure occurs during transactional processing,
database changes associated with the transaction shall be rolled back.

### NFR-011 — Persistent Data

Orders and executed trades shall be persisted in PostgreSQL so
that trading records remain available after application restart.

---

## 5. Data Integrity

### NFR-012 — Order State Consistency

An order's status and remaining quantity shall remain consistent
with its execution state.

### NFR-013 — Trade Integrity

Every persisted trade shall reference the corresponding BUY and
SELL orders involved in the execution.

### NFR-014 — Deterministic Matching

The matching engine shall apply deterministic Price-Time Priority
when selecting eligible opposite-side orders.

---

## 6. Maintainability

### NFR-015 — Layered Architecture

The application shall follow a layered architecture separating:

- Controllers
- Services
- Repositories
- Entities
- Security
- Exception Handling

### NFR-016 — Separation of Responsibilities

Business logic shall remain primarily within the service layer
rather than being implemented directly inside REST controllers.

### NFR-017 — Centralized Exception Handling

Application exceptions shall be handled centrally to provide
consistent error responses.

### NFR-018 — Documentation

Major business and technical design decisions shall be documented
separately from the application source code.

---

## 7. Extensibility

### NFR-019 — Matching Engine Extensibility

The matching engine should be structured so that additional
order types or matching rules can be introduced in future versions.

### NFR-020 — Future Scalability

The architecture should allow future introduction of technologies
such as asynchronous processing, messaging, caching, or distributed
components without requiring a complete redesign of the business model.

These capabilities are not part of Version 1.

---

## 8. Observability

### NFR-021 — Error Visibility

Application failures shall produce appropriate error responses
that allow clients to understand the nature of the failure.

### NFR-022 — Benchmark Visibility

Performance testing shall provide measurable metrics such as:

- Request count
- Error count
- Throughput
- Average response time
- Maximum response time

---

## 9. Analytics Availability

### NFR-023 — Analytical Data Access

Persisted order and trade data shall be available for analytical
processing.

### NFR-024 — Power BI Integration

The PostgreSQL data shall be consumable by Power BI for visualizing
order and trade metrics.

---

## 10. Development Environment

### NFR-025 — Database

PostgreSQL shall be used as the primary relational database.

### NFR-026 — Backend Platform

The application shall run on Java 21 with Spring Boot.

### NFR-027 — API Communication

The backend shall expose REST APIs for interaction with clients.

---

## 11. Version 1 Constraints

The following non-functional capabilities are intentionally outside
the scope of Version 1:

- Production-grade high-frequency trading optimization
- Guaranteed 10,000+ requests per second throughput
- Distributed order matching
- Dedicated matching-engine worker pools
- Asynchronous order processing
- Kafka-based event processing
- Redis-based caching
- WebSocket-based real-time updates
- Multi-node deployment
- Horizontal auto-scaling

---

## 12. Quality Goals

The primary quality goals of Version 1 are:

1. Correct order matching
2. Deterministic Price-Time Priority
3. Transactional data consistency
4. Secure authentication
5. Maintainable backend architecture
6. Reliable persistence
7. Testable business logic
8. Analytics-ready trading data