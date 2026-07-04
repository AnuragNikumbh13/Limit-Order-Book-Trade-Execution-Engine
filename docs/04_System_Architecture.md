# System Architecture

## 1. Overview

The High-Frequency Trading (HFT) Order Matching Engine follows a layered architecture to ensure separation of concerns,
scalability, maintainability, and thread-safe order processing.

---

## 2. High-Level Components

The system consists of the following major components:

- Authentication Layer
- Order Management Layer
- Matching Engine
- Order Books
- Portfolio Management
- Audit Logging
- Database Layer
- Analytics Layer

---

## 3. Request Flow

User

↓

JWT Authentication Filter

↓

Order Controller

↓

Order Service

↓

Business Validation

↓

Blocking Queue

↓

Matching Engine Thread

↓

BUY / SELL Order Books

↓

Trade Execution

↓

Portfolio Update

↓

Audit Logging

↓

PostgreSQL Database

---

## 4. Component Responsibilities

### JWT Authentication Filter

- Authenticates every incoming request.
- Validates JWT tokens.
- Rejects unauthorized requests.

---

### Order Controller

- Receives REST API requests.
- Delegates business logic to the service layer.

---

### Order Service

- Performs business validations.
- Verifies balances and stock ownership.
- Places valid orders into the Blocking Queue.

---

### Blocking Queue

- Stores validated incoming orders.
- Decouples API request handling from order matching.
- Enables asynchronous processing.

---

### Matching Engine

- Continuously consumes orders from the Blocking Queue.
- Applies Price-Time Priority.
- Executes trades.
- Updates Order Books.

---

### Order Books

- Maintain pending BUY and SELL Limit Orders.
- Preserve price priority.
- Preserve FIFO for orders at the same price.

---

### Portfolio Service

- Updates buyer holdings.
- Updates seller holdings.
- Updates available balances.

---

### Audit Logging

- Records every important business event.
- Maintains complete transaction history.

---

### PostgreSQL

Stores:

- Users
- Orders
- Trades
- Portfolios
- Audit Logs

---

## 5. Threading Model

API requests are processed independently.

Order matching is performed asynchronously by the Matching Engine thread.

This separation improves responsiveness and enables concurrent order processing.

---

## 6. Design Principles

The architecture follows the following principles:

- Layered Design
- Separation of Concerns
- Thread Safety
- Scalability
- Maintainability
- Extensibility