# System Architecture

## 1. Overview

The Limit Order Book & Trade Execution Engine follows a layered
architecture that separates API handling, business logic,
order matching, data persistence, authentication, and analytics.

The system is implemented as a synchronous Spring Boot application.
Order matching is performed during the order placement request
within the same application flow.

---

## 2. High-Level Components

The system consists of the following major components:

- Authentication Layer
- User Management Layer
- Order Management Layer
- Matching Engine
- Trade Management Layer
- Exception Handling Layer
- PostgreSQL Database
- Power BI Analytics Layer

---

## 3. High-Level Architecture

```text
                         Client
                           |
                           v
                 Spring Boot REST APIs
                           |
                           v
              JWT Authentication Filter
                           |
             +-------------+-------------+
             |                           |
             v                           v
       User Controller            Order Controller
             |                           |
             v                           v
       User Service               Order Service
             |                           |
             v                           v
      User Repository          Order Matching Service
             |                           |
             |                  +--------+--------+
             |                  |                 |
             |                  v                 v
             |          Order Repository   Trade Repository
             |                  |                 |
             +------------------+-----------------+
                                |
                                v
                           PostgreSQL
                                |
                                v
                            Power BI