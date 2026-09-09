# Matching Engine Design

## 1. Introduction

The Matching Engine is the core component of the
Limit Order Book & Trade Execution Engine.

It is responsible for processing incoming BUY and SELL Limit Orders,
identifying compatible opposite-side orders, executing trades,
updating order quantities and maintaining order states.

The matching process is implemented synchronously within the
Spring Boot application.

---

## 2. Matching Condition

A trade can be executed only when the incoming order price is
compatible with the price of an existing opposite-side order.

For a BUY order:

BUY Price >= SELL Price

For a SELL order:

SELL Price <= BUY Price

If the condition is not satisfied, the incoming order remains
OPEN with its remaining quantity unchanged.

---

## 3. Matching Priority

The Matching Engine follows Price-Time Priority as the intended
matching rule.

Priority is determined by:

1. Best Price
2. Earlier eligible order

For BUY orders, the engine considers SELL orders starting from
the lowest available price.

For SELL orders, the engine considers BUY orders starting from
the highest available price.

Orders at compatible prices are processed sequentially.

---

## 4. Resting and Incoming Orders

### Incoming Order

The incoming order is the newly submitted order that is currently
being processed by the Matching Engine.

### Resting Order

A resting order is an existing opposite-side order that was already
stored in the system before the incoming order was processed.

The incoming order is matched against eligible resting orders.

---

## 5. Execution Price Rule

A successful trade is executed at the price of the
resting order.

For example:

If a BUY order arrives at 2000 and an existing SELL order
is available at 1900:

BUY Price = 2000
SELL Price = 1900

Since:

2000 >= 1900

the orders can be matched.

The executed trade price is:

1900

because the SELL order is the resting order.

---

## 6. Partial Order Execution

The Matching Engine supports partial execution.

The matched quantity is calculated as:

Matched Quantity =
minimum(Incoming Remaining Quantity, Existing Remaining Quantity)

### Case 1 — Incoming Order Fully Filled

If the incoming order's remaining quantity becomes zero,
the incoming order is marked:

FILLED

### Case 2 — Existing Order Fully Filled

If the existing order's remaining quantity becomes zero,
the existing order is marked:

FILLED

### Case 3 — Partial Fill

If an order still has remaining quantity after execution,
it is marked:

PARTIALLY_FILLED

The remaining quantity can continue to participate in
subsequent matching.

---

## 7. Multiple Order Execution

A single incoming order can execute against multiple
compatible opposite-side orders.

Example:

Incoming BUY Order:

Quantity = 10

Available SELL Orders:

SELL 1 → Quantity = 2
SELL 2 → Quantity = 5
SELL 3 → Quantity = 3

The engine can execute:

Trade 1 → Quantity = 2
Trade 2 → Quantity = 5
Trade 3 → Quantity = 3

Total Executed Quantity = 10

The incoming BUY order becomes FILLED.

---

## 8. Order Status Lifecycle

Orders maintain the following lifecycle:

ORDER_PLACED
↓
OPEN
↓
PARTIALLY_FILLED
↓
FILLED

### ORDER_PLACED

The initial status assigned when an order is created.

### OPEN

The order is active and has remaining quantity available
for matching.

### PARTIALLY_FILLED

A portion of the order quantity has been executed,
but some quantity remains.

### FILLED

The complete order quantity has been executed and
remaining quantity is zero.

An unmatched order remains OPEN.

---

## 9. Matching Flow

The current matching flow is:

Incoming Order

↓

Authenticated User

↓

Create Order

↓

Persist Order

↓

Set Order Status to OPEN

↓

Identify Opposite Order Type

↓

Retrieve Eligible Opposite Orders

↓

Check Price Compatibility

↓

Calculate Matched Quantity

↓

Update Remaining Quantities

↓

Update Order Statuses

↓

Create Trade Record

↓

Persist Order and Trade Changes

↓

Continue Matching Until:

- Incoming order is FILLED, or
- No compatible opposite order remains

---

## 10. Order Retrieval Strategy

The Matching Engine retrieves eligible opposite-side orders
based on:

- Same trading symbol
- Opposite order type
- Active order statuses

Active statuses are:

- OPEN
- PARTIALLY_FILLED

For BUY orders, compatible SELL orders are considered from
lower prices first.

For SELL orders, compatible BUY orders are considered from
higher prices first.

---

## 11. Trade Creation

For every successful match, the engine creates a Trade record
containing:

- Trade ID
- Symbol
- Executed price
- Executed quantity
- Buy Order
- Sell Order

Each individual match generates a separate Trade record.

---

## 12. Transaction Management

Order placement and matching are executed within a transactional
boundary using Spring's `@Transactional`.

The transaction covers the related database operations involved
in order processing and trade execution.

If an unchecked exception occurs during the transaction,
the associated database changes are rolled back.

This helps maintain consistency between order states and
trade records.

---

## 13. Version 1 Scope

Version 1 supports:

- BUY Limit Orders
- SELL Limit Orders
- Price-Time based matching
- Partial Order Execution
- Multiple executions for a single order
- Order Status Management
- Trade Recording
- Order Retrieval
- Trade Retrieval
- Transactional Order Processing

Version 1 does not support:

- Account Balance Reservation
- Trade Settlement
- Portfolio / Holdings Management
- Deposits and Withdrawals
- Order Cancellation
- Market Orders
- Stop Loss Orders
- Iceberg Orders
- IOC Orders
- FOK Orders
- Distributed / Asynchronous Matching
- High-Frequency Production-Scale Optimization

---

## 14. Design Goals

The Matching Engine is designed to be:

- Deterministic
- Consistent
- Easy to understand
- Easy to extend
- Transactionally safe
- Analytics-friendly

The current implementation prioritizes correctness and
clarity of the core matching workflow rather than
production-scale high-frequency optimization.