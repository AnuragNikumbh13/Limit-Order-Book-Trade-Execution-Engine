# Matching Engine Design

## 1. Introduction

The Matching Engine is the core component of the High-Frequency Trading (HFT) Order Matching Engine.
It is responsible for processing incoming buy and sell orders, identifying compatible orders, executing trades,
and updating the order books while maintaining fairness and consistency.

---

## 2. Matching Condition

A trade can only be executed when the following condition is satisfied:

BUY Price >= SELL Price

If the condition is not satisfied, the incoming order remains in the corresponding Order Book until a compatible order arrives or the order is cancelled.

---

## 3. Matching Priority

The Matching Engine follows the Price-Time Priority rule.

Priority Order:

1. Best Price
2. Earliest Order (FIFO)

This ensures that orders with better prices are executed first, 
and among orders with the same price, the earliest order is executed before later orders.

---

## 4. Execution Price Rule

The trade shall be executed at the price of the Resting Order.

Resting Order:
The order that already exists in the Order Book before the incoming order arrives.

Incoming Order:
The newly received order that attempts to match with existing orders.

---

## 5. Partial Order Execution

If the quantity of the incoming order is greater than the quantity of the matched order, 
the matched order is completely executed and removed from the Order Book.

The remaining quantity of the incoming order continues matching with the next eligible order.

If the incoming order still has remaining quantity after all possible matches,
the remaining quantity is stored in the appropriate Order Book.

---

## 6. Order Books

The system maintains two independent Order Books.

- BUY Order Book
- SELL Order Book

BUY Order Book stores all pending BUY Limit Orders.

SELL Order Book stores all pending SELL Limit Orders.

---

## 7. Matching Flow

Incoming Order

↓

Business Validation

↓

Matching Condition Check

↓

Price-Time Priority Matching

↓

Trade Execution

↓

Portfolio Update

↓

Audit Logging

↓

Database Persistence

---

## 8. Version 1 Scope

Version 1 supports:

- BUY Limit Orders
- SELL Limit Orders
- Price-Time Priority
- Partial Order Execution
- Order Cancellation
- Trade History
- Portfolio Updates

Version 1 does not support:

- Market Orders
- Stop Loss Orders
- Iceberg Orders
- IOC Orders
- FOK Orders

---

## 9. Design Goals

The Matching Engine should be:

- Fair
- Deterministic
- Thread-safe
- Scalable
- Easy to extend
- Analytics-friendly