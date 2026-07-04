# Design Decisions

## Decision 1 - Order Matching Rule

### Decision

A trade can only be executed when:

BUY Price >= SELL Price

### Reason

This ensures that the buyer is willing to pay at least the seller's asking price.

---

## Decision 2 - Execution Price

### Decision

Trades are executed at the price of the Resting Order.

### Reason

The resting order entered the market earlier and therefore receives execution priority.
This closely follows the behavior of modern electronic exchanges.

---

## Decision 3 - Matching Priority

### Decision

The Matching Engine follows Price-Time Priority.

Priority Order:

1. Best Price
2. FIFO (First In First Out)

### Reason

This ensures fair execution while rewarding better prices and earlier orders.

---

## Decision 4 - Partial Order Execution

### Decision

Partial execution is supported.

### Reason

Large orders should be able to match against multiple smaller orders without waiting for an exact quantity match.

---

## Decision 5 - Balance Management

### Decision

Funds are blocked when a BUY order is accepted.

### Reason

This prevents users from placing multiple orders using the same balance.

---

## Decision 6 - Share Management

### Decision

Shares are blocked when a SELL order is accepted.

### Reason

This prevents users from selling the same shares multiple times.

---

## Decision 7 - Order Book Storage

### Decision

The Order Book is maintained in memory.

### Reason

Memory access is significantly faster than database access, making it suitable for high-frequency order matching.

---

## Decision 8 - Database Persistence

### Decision

Every accepted order is stored in PostgreSQL before entering the Matching Engine.

### Reason

This guarantees durability and allows recovery after an application restart.

---

## Decision 9 - Crash Recovery

### Decision

On application startup, all pending orders are loaded from the database and used to rebuild the in-memory Order Books.

### Reason

This ensures that no valid pending order is lost after an unexpected shutdown.

---

## Decision 10 - Asynchronous Processing

### Decision

Incoming orders are processed using a BlockingQueue.

### Reason

This separates API request handling from order matching, improving responsiveness and supporting concurrent processing. 