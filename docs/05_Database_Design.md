                        # Database Design

## 1. Overview

The HFT Order Matching Engine uses PostgreSQL as the primary database for persisting users, 
portfolios, orders, executed trades, and audit logs.

The database is designed to ensure data consistency, maintain transaction history, 
and support future analytics.

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

One User can have one Portfolio.

One User can place many Orders.

One Order may generate one or more Trades.

Every important business event is stored in the Audit Log.

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

Version 1 does not include:

- Live Market Data
- Watchlists
- Notifications
- Market Depth Snapshots