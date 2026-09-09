# Testing Strategy

## 1. Introduction

This document defines the testing strategy for the
Limit Order Book & Trade Execution Engine.

Testing focuses on validating order matching correctness,
transactional consistency, security, API behavior, and
performance under concurrent load.

---

## 2. Testing Scope

The Version 1 testing scope includes:

- User registration
- User authentication
- JWT-based security
- Order placement
- Order matching
- Price-Time Priority
- Partial order execution
- Multiple executions
- Order status management
- Trade creation
- Transaction rollback
- REST API behavior
- Load testing

---

## 3. Functional Testing

### 3.1 Unmatched Order

A BUY order is placed at a price lower than the available
SELL orders.

Expected result:

- Order remains `OPEN`
- Remaining quantity remains unchanged
- No Trade is created

---

### 3.2 Exact Match

A BUY and SELL order with compatible price and quantity are matched.

Expected result:

- BUY order becomes `FILLED`
- SELL order becomes `FILLED`
- Remaining quantity becomes zero
- A Trade record is created

---

### 3.3 Partial Fill — Existing Order

An incoming order partially consumes an existing opposite-side order.

Expected result:

- Incoming order becomes `FILLED` if completely consumed
- Existing order becomes `PARTIALLY_FILLED`
- Existing order retains its remaining quantity
- A Trade record is created for the executed quantity

---

### 3.4 Partial Fill — Incoming Order

An incoming order is only partially matched by an existing order.

Expected result:

- Incoming order becomes `PARTIALLY_FILLED`
- Remaining quantity is updated
- Existing order becomes `FILLED` if completely consumed
- A Trade record is created

---

### 3.5 Multiple Executions

A single incoming order is matched against multiple compatible
opposite-side orders.

Expected result:

- Multiple Trade records are created
- Each Trade represents one execution
- Remaining quantity is updated after every execution
- Incoming order becomes `FILLED` when fully executed

---

## 4. Price-Time Priority Testing

The matching engine is tested using multiple orders having
the same price.

Expected behavior:

1. Best available price receives priority.
2. If prices are equal, the earlier eligible order receives priority.

This verifies deterministic Price-Time Priority behavior.

---

## 5. Order Status Testing

The following statuses are validated:

- `ORDER_PLACED`
- `OPEN`
- `PARTIALLY_FILLED`
- `FILLED`

The tests verify that order status remains consistent with
the remaining quantity and execution state.

---

## 6. Trade Testing

Trade creation is validated after successful matches.

The tests verify:

- Trade ID generation
- Symbol
- Execution price
- Execution quantity
- BUY order reference
- SELL order reference

Multiple executions are verified to ensure that each execution
creates a separate Trade record.

---

## 7. Security Testing

Security-related scenarios tested include:

### Registration

- Successful registration
- Duplicate email
- Duplicate username
- Invalid registration data

### Login

- Successful login
- Incorrect password
- Non-existent email
- Invalid login request

### Protected APIs

- Valid JWT allows access
- Missing authentication prevents access

---

## 8. Exception Handling Testing

The centralized exception handling is tested for:

- Duplicate user registration
- Invalid credentials
- User not found
- Validation failures
- Missing resources
- Unauthorized requests

The API is expected to return appropriate HTTP status codes
and consistent error responses.

---

## 9. Transaction Rollback Testing

Transaction rollback was tested by intentionally triggering
an unchecked exception during order processing.

Expected behavior:

- Order changes are rolled back
- Trade changes are rolled back
- No partial transaction state remains persisted

The temporary test exception was removed after verification.

---

## 10. API Testing

The REST APIs were tested for:

- User registration
- Login
- Order placement
- Order retrieval
- Trade retrieval

Testing was performed using HTTP requests and verified
against the PostgreSQL database.

---

## 11. Load Testing

Apache JMeter was used to evaluate the application under
concurrent request load.

Test configuration:

- 200 concurrent threads
- 250 iterations per thread
- 50,000 total HTTP requests

Observed result:

```text
Total Requests : 50,000
Errors         : 0
Throughput     : 112.3 requests/second
Average Time   : 1,727 ms
Maximum Time   : 5,608 ms
```

These results represent the local development environment
and should not be interpreted as production-scale trading
system performance.

---

## 12. Test Summary

The Version 1 implementation has been manually validated for:

- Order matching
- Price-Time Priority
- Partial fills
- Multiple executions
- Order status transitions
- Trade creation
- JWT authentication
- Password validation
- Request validation
- Exception handling
- Transaction rollback
- REST API behavior
- Concurrent load behavior

---

## 13. Testing Limitations

Version 1 does not currently include:

- Automated unit test suite
- Automated integration test suite
- Distributed load testing
- Production-scale performance testing
- Chaos testing
- Multi-node concurrency testing

These areas can be introduced in future versions.