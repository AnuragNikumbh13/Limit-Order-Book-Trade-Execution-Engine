# Database Design

## 1. Overview

The Limit Order Book & Trade Execution Engine uses PostgreSQL
as the primary relational database for persistent storage.

The database stores user information, submitted orders, and
executed trades.

Spring Data JPA and Hibernate are used to map the application
entities to PostgreSQL tables.

---

## 2. Database Tables

The current system consists of the following tables:

- users
- orders
- trade

---

## 3. Relationships

The database follows these relationships:

- One User can place many Orders.
- An Order belongs to one User.
- A Trade references one BUY Order.
- A Trade references one SELL Order.
- An Order can be referenced by multiple Trade records when
  partial or multiple executions occur.

---

## 4. Primary Design Goals

The database design focuses on:

- Data Consistency
- Persistent Order and Trade Records
- Referential Integrity
- Transactional Processing
- Analytics-ready Storage
- Clear Entity Relationships

---

# 5. Table Structures

## Users

| Column | Description |
|---------|-------------|
| id | Unique User ID |
| username | Unique username |
| email | Unique registered email |
| password | BCrypt-hashed password |
| available_balance | Initial available balance assigned to the user |
| blocked_balance | Balance reserved for trading; currently initialized but not used for reservation |
| created_at | Account creation timestamp |
| updated_at | Last update timestamp |

### Constraints

- `id` is the Primary Key.
- `username` is unique and cannot be null.
- `email` is unique and cannot be null.
- `password` cannot be null.
- `available_balance` cannot be null.
- `blocked_balance` cannot be null.

---

## Orders

| Column | Description |
|---------|-------------|
| id | Unique Order ID |
| user_id | User who placed the order |
| symbol | Trading symbol such as TCS or INFY |
| order_type | BUY / SELL |
| quantity | Original order quantity |
| remaining_quantity | Quantity remaining after executions |
| price | Limit price of the order |
| order_status | Current order status |
| created_at | Order creation timestamp |

### Constraints and Relationships

- `id` is the Primary Key.
- `user_id` is a Foreign Key referencing `users.id`.
- `order_type` represents BUY or SELL.
- `order_status` represents the current lifecycle state.
- `remaining_quantity` tracks the unexecuted portion of the order.

---

## Trade

| Column | Description |
|---------|-------------|
| id | Unique Trade ID |
| symbol | Traded symbol |
| price | Execution price |
| quantity | Executed quantity |
| buy_order_id | Reference to the BUY order |
| sell_order_id | Reference to the SELL order |

### Relationships

- `buy_order_id` references the corresponding BUY Order.
- `sell_order_id` references the corresponding SELL Order.
- A single Order can be associated with multiple Trade records
  when multiple executions occur.

---

# 6. Entity Relationships

```text
                    User
                     │
                     │ 1
                     │
                     │ N
                     ▼
                   Orders
                  /      \
                 /        \
                ▼          ▼
          BUY Order      SELL Order
                \          /
                 \        /
                  ▼      ▼
                    Trade