`# Database Design

## 1. Overview

The HFT Order Matching Engine uses PostgreSQL as the primary database for persisting users, portfolios, orders, executed trades, and audit logs.

The database is designed to ensure data consistency, maintain transaction history, and support future analytics.

---

## 2. Database Tables

The system consists of the following tables:

- users
- portfolios
- orders
- trades
- audit_logs

---

## 3. Relationships

- One User can have one Portfolio.
- One User can place many Orders.
- One Order may generate one or more Trades.
- Every important business event is stored in the Audit Log.

---

## 4. Primary Design Goals

- Data Consistency
- ACID Compliance
- Transaction History
- Analytics-ready Storage
- Efficient Query Performance

---

## 5. Version 1 Scope

The database stores:

- User Information
- Portfolio Details
- Buy & Sell Orders
- Executed Trades
- Audit Logs

Version 1 does **not** include:

- Live Market Data
- Watchlists
- Notifications
- Market Depth Snapshots

---

# 6. Table Structures

## Users

| Column | Description |
|---------|-------------|
| id | Unique User ID |
| username | User's username |
| email | Registered email |
| password | Encrypted password |
| available_balance | Available funds for trading |
| blocked_balance | Funds reserved for pending BUY orders |
| created_at | Account creation timestamp |
| updated_at | Last profile update timestamp |

---

## Orders

| Column | Description |
|---------|-------------|
| id | Unique Order ID |
| user_id | User who placed the order |
| stock_symbol | Stock ticker (e.g., TCS) |
| order_type | BUY / SELL |
| limit_price | User's limit price |
| quantity | Original order quantity |
| remaining_quantity | Quantity yet to be matched |
| status | PENDING / PARTIALLY_FILLED / COMPLETED / CANCELLED |
| created_at | Order creation timestamp |
| updated_at | Last modification timestamp |

---

## Trades

| Column | Description |
|---------|-------------|
| id | Unique Trade ID |
| buy_order_id | BUY order reference |
| sell_order_id | SELL order reference |
| buyer_id | Buyer reference |
| seller_id | Seller reference |
| stock_symbol | Traded stock |
| execution_price | Trade execution price |
| quantity | Executed quantity |
| trade_value | execution_price × quantity |
| trade_time | Trade execution timestamp |

---

## Portfolio

| Column | Description |
|---------|-------------|
| id | Portfolio ID |
| user_id | Owner of the portfolio |
| stock_symbol | Stock ticker |
| total_quantity | Total owned shares |
| available_quantity | Shares available for selling |
| blocked_quantity | Shares reserved for pending SELL orders |
| average_buy_price | Volume Weighted Average Price (VWAP) |
| updated_at | Last portfolio update timestamp |

---

## Audit Logs

| Column | Description |
|---------|-------------|
| id | Audit Log ID |
| user_id | User reference |
| event_type | Type of business event |
| description | Detailed event description |
| created_at | Event timestamp |

---

# 7. Entity Relationships

```text
                  User
                   │
        ┌──────────┼──────────┐
        │          │          │
        ▼          ▼          ▼
   Portfolio     Orders    Audit Logs
                     │
                     ▼
                  Trades
```

Relationship Summary:

- One User → One Portfolio
- One User → Many Orders
- One User → Many Audit Logs
- One Order → One or More Trades

---

# 8. Important Design Decisions

- Orders remain mutable **only** for `remaining_quantity` and `status` until they are fully completed or cancelled.
- Trade records are immutable and permanently stored.
- The Portfolio stores only the current holdings using **VWAP (Volume Weighted Average Price)**.
- Complete execution history is maintained in the `trades` table.
- Funds are blocked immediately after a BUY order is accepted.
- Shares are blocked immediately after a SELL order is accepted.
- Pending orders are reconstructed into the in-memory Order Books when the application starts after a crash or restart.
- The Order Book is maintained in memory for low-latency matching, while PostgreSQL provides durability and recovery.