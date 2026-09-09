# Concurrency Design

## 1. Introduction

This document describes the concurrency characteristics of the
Limit Order Book & Trade Execution Engine.

The current Version 1 implementation processes order placement
and matching synchronously within the Spring Boot application.

The design intentionally focuses on correctness, deterministic
matching, and transactional consistency rather than
production-scale concurrent order processing.

---

## 2. Current Concurrency Model

The current implementation follows a synchronous request-processing model.

The order processing flow is:

```text
HTTP Request
     |
     v
OrderController
     |
     v
OrderService
     |
     v
OrderMatchingService
     |
     v
PostgreSQL
```

The matching operation is performed as part of the request-processing
flow.

The system does not currently use:

- Dedicated matching-engine worker threads
- BlockingQueue
- Producer-Consumer architecture
- Kafka-based event processing
- Asynchronous order matching
- Distributed matching workers

---

## 3. Synchronous Matching

When an authenticated user submits an order, the request is processed
synchronously.

The application:

1. Receives the order request.
2. Creates the Order entity.
3. Persists the order.
4. Invokes the Matching Engine.
5. Retrieves eligible opposite-side orders.
6. Applies Price-Time Priority.
7. Executes compatible matches.
8. Updates order quantities and statuses.
9. Creates Trade records.
10. Persists the resulting changes.

The request-processing thread remains involved during this workflow.

Therefore, the current implementation does not decouple
order submission from order matching.

---

## 4. Transactional Boundary

Order placement and matching are executed within a transactional
boundary using Spring's `@Transactional`.

The primary transactional flow is:

```text
placeOrder()
     |
     | @Transactional
     v
Create Order
     |
     v
Persist Order
     |
     v
matchOrder()
     |
     v
Update Orders
     |
     v
Create Trade
     |
     v
Persist Changes
     |
     v
Commit Transaction
```

If an unchecked exception occurs before the transaction commits,
the associated database changes are rolled back.

The transaction provides database-level consistency for the
order and trade updates involved in the request.

---

## 5. Concurrent Requests

The application can receive multiple HTTP requests concurrently
because the web server can process requests using multiple request
threads.

However, the current matching implementation does not introduce
a dedicated concurrency-control mechanism around the matching logic.

This means that concurrent order submissions targeting the same
symbol may be processed by different request threads.

The current Version 1 design therefore prioritizes a simple,
synchronous implementation and does not claim to provide
production-grade concurrent matching guarantees.

---

## 6. Price-Time Priority and Concurrency

Price-Time Priority is implemented through deterministic ordering
of eligible opposite-side orders.

The repository retrieval strategy orders eligible orders by:

For incoming BUY orders:

```text
SELL Price ASC
Creation Time ASC
```

For incoming SELL orders:

```text
BUY Price DESC
Creation Time ASC
```

This ensures that, within a single matching operation:

1. The best available price is considered first.
2. At the same price, the earlier-created eligible order
   is considered first.

This ordering provides deterministic behavior for the matching
operation itself.

---

## 7. Database Transaction vs Thread Safety

The use of `@Transactional` should not be interpreted as making
the Matching Engine thread-safe.

`@Transactional` provides a database transaction boundary.

It helps ensure that related database operations either commit
together or roll back together when an unchecked failure occurs.

It does not by itself:

- Serialize all incoming orders
- Prevent concurrent matching operations
- Provide an in-memory lock
- Create a worker pool
- Guarantee exclusive access to an order
- Convert the application into an asynchronous system

Concurrency control and transaction management are separate concerns.

---

## 8. Current Concurrency Limitations

The current Version 1 implementation has the following limitations:

- Matching is synchronous.
- Matching is performed within the HTTP request flow.
- There is no dedicated matching worker pool.
- There is no queue between order submission and matching.
- There is no Kafka-based event-driven processing.
- There is no distributed matching coordination.
- The system has not been designed as a production-grade
  high-frequency trading engine.

These limitations are intentional for Version 1.

---

## 9. Load Testing and Concurrency

The application was evaluated using Apache JMeter with concurrent
HTTP requests.

The benchmark used:

```text
Concurrent Threads : 200
Iterations per Thread : 250
Total Requests : 50,000
Errors : 0
```

Observed local benchmark:

```text
Throughput : 112.3 requests/second
Average Response Time : 1,727 ms
Maximum Response Time : 5,608 ms
Error Rate : 0.00%
```

These results demonstrate successful processing of the tested
concurrent HTTP workload in the local development environment.

They do not represent production-grade high-frequency trading
performance or prove a specific throughput target.

---

## 10. Future Concurrency Improvements

Future versions may introduce stronger concurrency and scalability
mechanisms if higher throughput or distributed processing is required.

Potential improvements include:

- Producer-Consumer architecture
- Dedicated matching-engine worker threads
- BlockingQueue or other in-memory queues
- Symbol-based partitioning
- Asynchronous order processing
- Database locking strategies
- Optimistic or pessimistic locking where appropriate
- Kafka-based event processing
- Distributed matching architecture
- Horizontal scaling
- Redis-based supporting infrastructure

These mechanisms are not implemented in Version 1.

---

## 11. Design Decision

Version 1 intentionally uses synchronous order matching.

The primary reason is to keep the core trading workflow
simple, deterministic, testable, and easy to reason about.

The current architecture establishes the fundamental business
rules and persistence model first.

Concurrency and distributed processing can be introduced in
future versions after the correctness of the core matching
workflow has been established.

---

## 12. Version 1 Scope

The current concurrency design supports:

- Synchronous order processing
- Synchronous matching
- Transactional database updates
- Deterministic Price-Time ordering within a matching operation
- Concurrent HTTP request handling through the application server

The current version does not provide:

- Dedicated matching workers
- Producer-Consumer queues
- BlockingQueue-based matching
- Kafka event streaming
- Distributed matching
- Horizontal matching-engine scaling
- Production-grade high-frequency trading concurrency