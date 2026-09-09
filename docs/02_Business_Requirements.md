# Business Requirements

## 1. Introduction

This document defines the business requirements for the
Limit Order Book & Trade Execution Engine.

It describes the core capabilities that the system must provide
from a business perspective without focusing on technical
implementation details.

---

## 2. User Management

### BR-001 — User Registration

The system shall allow new users to register using a unique
username and email address.

### BR-002 — User Authentication

The system shall allow registered users to authenticate using
their email and password.

### BR-003 — Secure Credentials

The system shall securely store user credentials and prevent
unauthorized access to protected system functionality.

---

## 3. Order Management

### BR-004 — BUY Limit Orders

The system shall allow authenticated users to place BUY limit orders.

### BR-005 — SELL Limit Orders

The system shall allow authenticated users to place SELL limit orders.

### BR-006 — Order Validation

The system shall validate order requests before accepting them.

### BR-007 — Order Information

Each order shall maintain the following information:

- Order ID
- Associated user
- Trading symbol
- Order type
- Original quantity
- Remaining quantity
- Limit price
- Order status
- Creation timestamp

### BR-008 — Order Lifecycle

The system shall maintain the current status and remaining quantity
of each order throughout its lifecycle.

### BR-009 — Order History

The system shall allow retrieval of stored orders and order history.

---

## 4. Order Matching

### BR-010 — Price-Time Priority

The system shall match compatible BUY and SELL orders using
Price-Time Priority.

Priority shall be determined by:

1. Best available price
2. Earlier eligible order at the same price

### BR-011 — BUY Matching Condition

A BUY order shall be eligible for matching when:

```text
BUY Price >= SELL Price
```

### BR-012 — SELL Matching Condition

A SELL order shall be eligible for matching when:

```text
SELL Price <= BUY Price
```

### BR-013 — Partial Execution

The system shall support partial order execution when the quantities
of matching orders are different.

### BR-014 — Multiple Executions

The system shall allow a single incoming order to execute against
multiple compatible opposite-side orders.

### BR-015 — Unmatched Orders

Orders that cannot currently be matched shall remain OPEN with their
remaining quantity available for future matching.

---

## 5. Order Status Management

### BR-016 — Order Statuses

The system shall maintain the following order statuses:

- ORDER_PLACED
- OPEN
- PARTIALLY_FILLED
- FILLED

### BR-017 — Partial Fill Status

An order shall be marked `PARTIALLY_FILLED` when only a portion
of its quantity has been executed.

### BR-018 — Filled Status

An order shall be marked `FILLED` when its remaining quantity
reaches zero.

---

## 6. Trade Management

### BR-019 — Trade Execution

The system shall create a trade whenever compatible BUY and SELL
orders are successfully matched.

### BR-020 — Trade per Execution

The system shall generate a separate Trade record for every
successful execution.

### BR-021 — Trade Information

Each trade record shall contain:

- Trade ID
- Symbol
- Executed price
- Executed quantity
- Buy order
- Sell order

### BR-022 — Trade History

The system shall maintain trade history for executed trades.

---

## 7. Data Integrity

### BR-023 — Transactional Processing

Order placement and related trade execution shall be processed
as a single transactional operation.

### BR-024 — Rollback on Failure

If an unchecked failure occurs during transactional order processing,
the associated database changes shall be rolled back to maintain
data consistency.

---

## 8. Order & Trade Retrieval

### BR-025 — All Orders

The system shall provide access to all stored orders.

### BR-026 — Order by ID

The system shall allow retrieval of an individual order by its ID.

### BR-027 — Orders by User

The system shall allow retrieval of orders belonging to a specific user.

### BR-028 — All Trades

The system shall provide access to all stored trades.

### BR-029 — Trade by ID

The system shall allow retrieval of an individual trade by its ID.

---

## 9. Security

### BR-030 — Protected Business APIs

Only authenticated users shall be allowed to access protected
business functionality.

### BR-031 — Token-Based Authentication

The system shall use token-based authentication for protected APIs.

### BR-032 — Password Protection

User passwords shall not be stored in plain text.

---

## 10. Analytics & Reporting

### BR-033 — Analytics Data

The system shall provide structured order and trade data for analytics.

### BR-034 — Order & Trade Metrics

The system shall support analysis of:

- Total orders
- Total trades

### BR-035 — BUY / SELL Distribution

The system shall support analysis of BUY versus SELL order distribution.

### BR-036 — Order Status Distribution

The system shall support analysis of order status distribution.

### BR-037 — Trade Volume

The system shall support analysis of executed trade volume by symbol.