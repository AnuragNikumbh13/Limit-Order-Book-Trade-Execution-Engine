# API Design

## 1. Introduction

This document defines the REST API design for the
Limit Order Book & Trade Execution Engine.

The APIs provide functionality for user registration,
authentication, order management, and trade retrieval.

---

## 2. API Conventions

### Base URL

```text
http://localhost:8080
```

### Content Type

Requests containing a request body use:

```text
Content-Type: application/json
```

### Authentication

Protected APIs require a JWT in the Authorization header:

```text
Authorization: Bearer <JWT>
```

---

## 3. User Registration

### Endpoint

```http
POST /userRegistration
```

### Authentication

Public API.

### Request Body

```json
{
  "username": "Anurag",
  "email": "anurag@gmail.com",
  "password": "********"
}
```

### Description

Registers a new user in the system.

The password is BCrypt-hashed before persistence.

### Possible Responses

#### Success

```text
200 OK
```

#### Duplicate Username / Email

```text
409 Conflict
```

Example:

```json
{
  "message": "Email already registered"
}
```

#### Validation Error

```text
400 Bad Request
```

---

## 4. User Login

### Endpoint

```http
POST /login
```

### Authentication

Public API.

### Request Body

```json
{
  "email": "anurag@gmail.com",
  "password": "********"
}
```

### Description

Authenticates a registered user using email and password.

After successful authentication, the system generates
a JWT token.

### Success Response

```text
200 OK
```

Example:

```json
{
  "message": "Login Successful",
  "username": "Anurag",
  "token": "<JWT>"
}
```

### Possible Errors

#### Invalid Credentials

```text
401 Unauthorized
```

#### Email Not Found

```text
404 Not Found
```

#### Validation Error

```text
400 Bad Request
```

---

# 5. Order APIs

All order APIs require JWT authentication.

---

## 5.1 Place Order

### Endpoint

```http
POST /orders
```

### Authentication

Required.

### Request Body

```json
{
  "symbol": "TCS",
  "orderType": "BUY",
  "quantity": 10,
  "price": 1900
}
```

### Description

Places a BUY or SELL limit order for the authenticated user.

The order is persisted and passed to the matching engine
for processing.

### Possible Response

```text
200 OK
```

---

## 5.2 Retrieve All Orders

### Endpoint

```http
GET /orders
```

### Authentication

Required.

### Description

Returns all stored orders.

### Response

```text
200 OK
```

Example:

```json
[
  {
    "id": 1,
    "symbol": "TCS",
    "orderType": "BUY",
    "quantity": 10,
    "remainingQuantity": 5,
    "price": 1900,
    "orderStatus": "PARTIALLY_FILLED"
  }
]
```

---

## 5.3 Retrieve Order by ID

### Endpoint

```http
GET /orders/{id}
```

### Authentication

Required.

### Path Parameter

```text
id
```

Unique order identifier.

### Example

```http
GET /orders/15
```

### Description

Returns a specific order using its ID.

---

## 5.4 Retrieve Orders by User

### Endpoint

```http
GET /orders/user/{userId}
```

### Authentication

Required.

### Path Parameter

```text
userId
```

Unique user identifier.

### Example

```http
GET /orders/user/1
```

### Description

Returns orders associated with the specified user.

---

# 6. Trade APIs

All trade APIs require JWT authentication.

---

## 6.1 Retrieve All Trades

### Endpoint

```http
GET /trades
```

### Authentication

Required.

### Description

Returns all executed trades stored in the database.

### Response

```text
200 OK
```

Example:

```json
[
  {
    "id": 1,
    "symbol": "TCS",
    "price": 1900,
    "quantity": 5,
    "buyOrder": {},
    "sellOrder": {}
  }
]
```

---

## 6.2 Retrieve Trade by ID

### Endpoint

```http
GET /trades/{id}
```

### Authentication

Required.

### Path Parameter

```text
id
```

Unique trade identifier.

### Example

```http
GET /trades/1
```

### Description

Returns a specific trade using its ID.

---

# 7. Authentication Flow

The authentication flow is:

```text
Client
  |
  | POST /login
  v
UserController
  |
  v
UserService
  |
  | Verify email & password
  v
JWT Generation
  |
  v
JWT returned to Client
```

The client then includes the JWT when calling protected APIs.

```text
Client
  |
  | Authorization: Bearer <JWT>
  v
JwtAuthenticationFilter
  |
  | Validate JWT
  v
Protected Controller
  |
  v
Service Layer
```

---

# 8. Order Processing Flow

```text
POST /orders
      |
      v
JWT Authentication
      |
      v
OrderController
      |
      v
OrderService
      |
      v
Create Order
      |
      v
OrderMatchingService
      |
      v
Find Eligible Opposite Orders
      |
      v
Price-Time Priority
      |
      v
Price Compatibility Check
      |
      v
Calculate Matched Quantity
      |
      v
Update Order Status / Remaining Quantity
      |
      v
Create Trade
      |
      v
PostgreSQL
```

The current implementation processes order matching synchronously.

---

# 9. API Security

The following endpoints are publicly accessible:

```text
POST /userRegistration
POST /login
```

All other business endpoints require authentication.

The application uses:

- JWT authentication
- Stateless sessions
- BCrypt password hashing
- Spring Security

---

# 10. Error Handling

The application uses centralized exception handling
for consistent API error responses.

Common error categories include:

- Validation errors
- Duplicate user registration
- Invalid credentials
- User not found
- Order not found
- Trade not found
- Unauthorized requests

---

# 11. API Scope

The current API implementation intentionally does not provide:

- Order cancellation
- Market orders
- Stop-loss orders
- Portfolio APIs
- Balance reservation APIs
- Trade settlement APIs
- Deposit / withdrawal APIs
- WebSocket APIs
- Live market-data APIs