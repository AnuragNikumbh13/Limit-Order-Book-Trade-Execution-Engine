# Functional Requirements

## 1. Introduction

This document defines the functional requirements of the
Limit Order Book & Trade Execution Engine.

The requirements describe the expected behavior of the system
and the functionality provided by each major component.

---

## 2. User Registration

### FR-001 — Register User

The system shall provide an API through which a new user can register.

The registration request shall contain:

- Username
- Email
- Password

### FR-002 — Validate Registration Data

The system shall validate the registration request before creating
the user.

The system shall reject requests containing invalid or missing
required fields.

### FR-003 — Unique Username

The system shall prevent registration when the requested username
already exists.

### FR-004 — Unique Email

The system shall prevent registration when the requested email
already exists.

### FR-005 — Password Hashing

The system shall hash the user's password before storing the user
in the database.

Plain-text passwords shall not be persisted.

---

## 3. User Login

### FR-006 — Login

The system shall provide a login API that accepts:

- Email
- Password

### FR-007 — User Verification

The system shall verify that the supplied email belongs to a
registered user.

### FR-008 — Password Verification

The system shall compare the supplied password with the stored
password hash.

### FR-009 — Authentication Token

After successful authentication, the system shall generate a JWT
authentication token.

### FR-010 — Invalid Credentials

The system shall reject authentication attempts when the supplied
credentials are invalid.

---

## 4. Authentication & Authorization

### FR-011 — Stateless Authentication

The application shall use stateless authentication for protected APIs.

### FR-012 — JWT Validation

For protected requests, the system shall inspect the Authorization
header and validate the supplied JWT.

Expected format:

```text
Authorization: Bearer <JWT>
```

### FR-013 — Authenticated Access

Requests containing a valid JWT shall be allowed to access
protected business APIs.

### FR-014 — Unauthenticated Access

Requests without valid authentication shall not be allowed to
access protected business APIs.

### FR-015 — Public APIs

The following APIs shall remain publicly accessible:

- User Registration
- Login

---

## 5. Order Placement

### FR-016 — Place Order

An authenticated user shall be able to place a BUY or SELL
limit order.

### FR-017 — Order Request

An order request shall contain:

- Symbol
- Order Type
- Quantity
- Limit Price

### FR-018 — Associate Order with User

The system shall associate every newly created order with the
authenticated user.

### FR-019 — Initial Order State

When an order is created, its initial status shall be:

```text
ORDER_PLACED
```

The order shall then be processed by the matching engine.

### FR-020 — Remaining Quantity

The remaining quantity of a newly created order shall initially
be equal to its original quantity.

---

## 6. Order Matching

### FR-021 — Identify Opposite Orders

For every incoming order, the matching engine shall identify
eligible active orders from the opposite order type.

Active orders shall have one of the following statuses:

- OPEN
- PARTIALLY_FILLED

### FR-022 — BUY Matching

For an incoming BUY order, the system shall consider SELL orders
whose price is less than or equal to the BUY order price.

```text
BUY Price >= SELL Price
```

### FR-023 — SELL Matching

For an incoming SELL order, the system shall consider BUY orders
whose price is greater than or equal to the SELL order price.

```text
SELL Price <= BUY Price
```

### FR-024 — Price-Time Priority

Eligible opposite-side orders shall be processed according to
Price-Time Priority.

Priority shall be determined by:

1. Best available price
2. Earlier order creation time at the same price

### FR-025 — Execution Price

The execution price shall be determined using the resting
opposite-side order's price.

### FR-026 — Match Quantity

The executed quantity shall be the smaller of the remaining
quantities of the two matching orders.

```text
Matched Quantity =
min(Incoming Remaining Quantity,
    Existing Remaining Quantity)
```

---

## 7. Partial Order Execution

### FR-027 — Partial Fill of Existing Order

If the incoming order consumes only part of an existing order,
the existing order shall remain active with its updated
remaining quantity.

### FR-028 — Partial Fill of Incoming Order

If the incoming order cannot be completely filled, it shall remain
active with its updated remaining quantity.

### FR-029 — Multiple Executions

If required, an incoming order shall continue matching against
additional eligible opposite-side orders until:

- Its remaining quantity becomes zero, or
- No compatible order remains.

---

## 8. Order Status Management

### FR-030 — OPEN Status

An order shall be marked `OPEN` when it has remaining quantity
and is available for future matching.

### FR-031 — PARTIALLY_FILLED Status

An order shall be marked `PARTIALLY_FILLED` when part of its
original quantity has been executed and remaining quantity exists.

### FR-032 — FILLED Status

An order shall be marked `FILLED` when its remaining quantity
reaches zero.

### FR-033 — Status Consistency

The order status shall remain consistent with the order's
remaining quantity and execution state.

---

## 9. Trade Creation

### FR-034 — Create Trade

Whenever a successful order match occurs, the system shall create
a Trade record.

### FR-035 — Trade Details

Each Trade shall contain:

- Trade ID
- Symbol
- Execution Price
- Execution Quantity
- Buy Order
- Sell Order

### FR-036 — Multiple Trade Records

When one incoming order executes against multiple opposite-side
orders, the system shall create a separate Trade record for
each execution.

---

## 10. Transaction Processing

### FR-037 — Atomic Order Processing

Order placement and the associated matching/trade updates shall
be processed within a database transaction.

### FR-038 — Rollback

If an unchecked failure occurs during the transaction, the
associated database changes shall be rolled back.

This prevents partially persisted order/trade state.

---

## 11. Order Retrieval

### FR-039 — Retrieve All Orders

The system shall provide an API to retrieve all stored orders.

### FR-040 — Retrieve Order by ID

The system shall provide an API to retrieve an order using
its order ID.

### FR-041 — Retrieve User Orders

The system shall provide an API to retrieve orders associated
with a specific user.

---

## 12. Trade Retrieval

### FR-042 — Retrieve All Trades

The system shall provide an API to retrieve all executed trades.

### FR-043 — Retrieve Trade by ID

The system shall provide an API to retrieve an individual trade
using its trade ID.

---

## 13. Exception Handling

### FR-044 — Centralized Exception Handling

The system shall provide centralized handling for application
exceptions.

### FR-045 — Validation Errors

Invalid request data shall result in an appropriate validation
error response.

### FR-046 — Business Errors

Business-level failures such as duplicate registration,
invalid credentials, or missing resources shall return
appropriate error responses.

---

## 14. Analytics

### FR-047 — Order Analytics

The system shall expose persisted order data for analytical use.

### FR-048 — Trade Analytics

The system shall expose persisted trade data for analytical use.

### FR-049 — Dashboard Metrics

The analytics layer shall support visualization of:

- Total Orders
- Total Trades
- BUY vs SELL Distribution
- Order Status Distribution
- Executed Trade Volume by Symbol

---

## 15. Functional Flow

The primary order-processing flow shall be:

```text
User
  ↓
Authenticate
  ↓
Receive JWT
  ↓
Place BUY / SELL Order
  ↓
Validate Request
  ↓
Create Order
  ↓
Matching Engine
  ↓
Find Eligible Opposite Orders
  ↓
Apply Price-Time Priority
  ↓
Check Price Compatibility
  ↓
Calculate Matched Quantity
  ↓
Update Order Quantities & Status
  ↓
Create Trade
  ↓
Persist Changes
  ↓
Return Result
```

---

## 16. Version 1 Functional Boundaries

The following functionality is intentionally not implemented
in Version 1:

- Order cancellation
- Market orders
- Stop-loss orders
- Balance reservation
- Trade settlement
- Portfolio management
- Deposits and withdrawals
- Live market data
- Asynchronous matching
- Distributed matching
- WebSocket notifications