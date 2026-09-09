[# Limit Order Book & Trade Execution Engine

## 1. Project Overview

The Limit Order Book & Trade Execution Engine is a backend system
that simulates the core order matching functionality of an electronic
trading platform.

The system allows authenticated users to place BUY and SELL limit orders
and automatically matches compatible orders using Price-Time Priority.

The system supports partial order execution, maintains order states,
records executed trades, and stores trading data in PostgreSQL for
retrieval and analytics.

---

## 2. Problem Statement

Electronic trading systems need to match compatible BUY and SELL orders
according to predefined trading rules while maintaining consistency
and accurate execution records.

This project aims to simulate the core matching workflow of such a system
by implementing a simplified Limit Order Book and Trade Execution Engine.

The engine evaluates incoming limit orders against eligible opposite-side
orders using Price-Time Priority and creates trade records whenever
a match occurs.

---

## 3. Project Goal

The goal of this project is to design and develop a backend
Order Matching Engine that:

- Accepts BUY and SELL limit orders
- Matches compatible orders using Price-Time Priority
- Supports partial order execution
- Allows one order to execute against multiple opposite orders
- Maintains order status and remaining quantity
- Records every successful execution as a Trade
- Stores order and trade data in PostgreSQL
- Provides secured REST APIs using JWT authentication
- Exposes trading data for analytics through Power BI

---

## 4. Project Scope

The project includes:

- User Registration
- User Login & JWT Authentication
- BCrypt Password Hashing
- BUY & SELL Limit Orders
- Order Matching Logic
- Price-Time Priority Matching
- Partial Order Execution
- Multiple Executions for a Single Order
- Order Status Management
- Trade Recording
- Order & Trade Retrieval APIs
- Transactional Order and Trade Processing
- Centralized Exception Handling
- PostgreSQL Data Storage
- Power BI Analytics Dashboard

---

## 5. Out of Scope

The following features are intentionally excluded from the
current version of the system:

- Account Balance Reservation
- Trade Settlement
- Portfolio / Holdings Management
- Deposits and Withdrawals
- Market Orders
- Stop-Loss Orders
- Live Stock Market Data
- Order Cancellation
- Distributed or Asynchronous Matching
- High-Frequency Production-Scale Optimization
- Microservices Architecture
- Kafka / Event Streaming
- Redis Caching
- WebSocket Live Market Updates

---

## 6. Target Users

The system is primarily designed for:

- Developers learning trading system architecture
- Backend developers exploring order matching systems
- Students studying financial technology (FinTech) systems
- Recruiters and technical reviewers evaluating backend design
  and implementation

---

## 7. Expected Outcome

The completed system provides a simplified simulation of the
core order matching workflow used in electronic trading systems.

It accepts authenticated BUY and SELL limit orders, evaluates
compatible orders using Price-Time Priority, executes trades with
support for partial fills and multiple executions, maintains
order states, and stores the resulting trading records in PostgreSQL.

The stored order and trade data is also made available for
analytics through a Power BI dashboard.]()