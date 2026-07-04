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


6. Table Structures
   Users
   Column	Description
   id	Unique User ID
   username	User's username
   email	Registered email
   password	Encrypted password
   available_balance	Available funds for trading
   blocked_balance	Funds reserved for pending BUY orders
   created_at	Account creation timestamp
   updated_at	Last profile update timestamp



   Orders
   Column	Description
   id	Unique Order ID
   user_id	User who placed the order
   stock_symbol	Stock ticker (e.g., TCS)
   order_type	BUY / SELL
   limit_price	User's limit price
   quantity	Original order quantity
   remaining_quantity	Quantity yet to be matched
   status	PENDING / PARTIALLY_FILLED / COMPLETED / CANCELLED
   created_at	Order creation time
   updated_at	Last modification time



   Trades
   Column	Description
   id	Unique Trade ID
   buy_order_id	BUY order reference
   sell_order_id	SELL order reference
   buyer_id	Buyer
   seller_id	Seller
   stock_symbol	Traded stock
   execution_price	Trade execution price
   quantity	Executed quantity
   trade_value	execution_price × quantity
   trade_time	Execution timestamp



   Portfolio
   Column	Description
   id	Portfolio ID
   user_id	Owner
   stock_symbol	Stock
   total_quantity	Total owned shares
   available_quantity	Shares available to sell
   blocked_quantity	Shares reserved for pending SELL orders
   average_buy_price	VWAP of current holdings
   updated_at	Last portfolio update


   Audit Logs
   Column	Description
   id	Audit entry ID
   user_id	User reference
   event_type	Event category
   description	Event details
   created_at	Event timestamp


7. Relationships (Visual)

User (1)
│
├───────────────┐
│               │
▼               ▼
Portfolio (1)     Orders (N)
│
▼
Trades (N)

User (1)
│
▼
Audit Logs (N)


8. Important Design Decisions
   • Orders remain immutable after execution except for
   remaining_quantity and status.

• Trade records are immutable.

• Portfolio stores only current holdings (VWAP based).

• Complete execution history is maintained in the Trade table.

• Funds are blocked during BUY order placement.

• Shares are blocked during SELL order placement.

• Pending orders are reconstructed into the in-memory Order Book during application startup.