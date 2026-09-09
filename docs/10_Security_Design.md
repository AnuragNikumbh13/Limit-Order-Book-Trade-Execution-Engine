# Security Design

## 1. Introduction

This document describes the security design of the
Limit Order Book & Trade Execution Engine.

The Version 1 application uses Spring Security with JWT-based
stateless authentication to protect business APIs.

User passwords are stored using BCrypt hashing and protected
endpoints require a valid JWT authentication token.

---

## 2. Security Objectives

The primary security objectives of Version 1 are:

- Protect business APIs from unauthenticated access
- Secure user passwords
- Authenticate users using JWT
- Maintain stateless authentication
- Validate JWTs before allowing protected requests
- Avoid exposing sensitive credentials and tokens
- Provide centralized handling for authentication-related failures

---

## 3. Authentication Model

The application uses token-based authentication.

The authentication flow is:

```text
User
  |
  | Email + Password
  v
POST /login
  |
  v
UserService
  |
  | Find User by Email
  | Verify Password
  v
JWT Generation
  |
  v
JWT returned to Client
```

The client must then provide the JWT when accessing
protected business APIs.

---

## 4. User Registration Security

User registration is provided through:

```http
POST /userRegistration
```

The registration API is publicly accessible because a user must
be able to create an account before authentication.

During registration:

1. The system checks whether the email already exists.
2. The system checks whether the username already exists.
3. The supplied password is encoded using BCrypt.
4. The encoded password is stored in PostgreSQL.

The plain-text password is never persisted.

---

## 5. Password Hashing

The application uses Spring Security's `PasswordEncoder`
with `BCryptPasswordEncoder`.

The password flow is:

```text
Plain Password
      |
      v
BCryptPasswordEncoder
      |
      v
BCrypt Hash
      |
      v
PostgreSQL
```

During login, the supplied password is compared with the
stored BCrypt hash using the password encoder.

The application does not decrypt passwords because BCrypt is
a one-way password hashing mechanism.

---

## 6. Login Security

The login API is:

```http
POST /login
```

The login request contains:

```json
{
  "email": "user@example.com",
  "password": "********"
}
```

The system:

1. Searches for the user using the supplied email.
2. Rejects the request if the email is not found.
3. Verifies the supplied password against the stored BCrypt hash.
4. Generates a JWT after successful authentication.
5. Returns the JWT to the client.

Example response:

```json
{
  "message": "Login Successful",
  "username": "user",
  "token": "<JWT>"
}
```

---

## 7. JWT Authentication

The application uses JSON Web Tokens for authentication
of protected requests.

The client sends the token using the HTTP Authorization header:

```text
Authorization: Bearer <JWT>
```

The authentication flow for a protected request is:

```text
Client
  |
  | Authorization: Bearer <JWT>
  v
JwtAuthenticationFilter
  |
  | Extract JWT
  v
Extract Username
  |
  v
Find User
  |
  v
Validate JWT
  |
  v
Set Authentication
  |
  v
Protected Controller
```

---

## 8. JWT Generation

JWTs are generated after successful user authentication.

The token contains information such as:

- Subject / username
- Issued-at timestamp
- Expiration timestamp

The token is signed using a configured secret key.

The application validates the token signature and expiration
before accepting the authenticated request.

---

## 9. JWT Authentication Filter

The application contains a custom
`JwtAuthenticationFilter`.

The filter extends:

```java
OncePerRequestFilter
```

This allows the JWT authentication logic to execute once
for each request.

The filter:

1. Reads the `Authorization` header.
2. Checks whether the header contains a Bearer token.
3. Extracts the JWT.
4. Extracts the username from the JWT.
5. Finds the corresponding user.
6. Validates the JWT.
7. Creates an authentication object when the token is valid.
8. Stores the authentication in the Spring Security context.
9. Continues the request through the filter chain.

If no Bearer token is present, the request continues through
the filter chain and protected endpoints are subsequently
handled according to the configured security rules.

---

## 10. Security Filter Chain

The application configures a Spring Security
`SecurityFilterChain`.

The security configuration establishes:

```text
CSRF Disabled
        |
        v
Stateless Session Management
        |
        v
Public Authentication Endpoints
        |
        v
All Other Endpoints Require Authentication
        |
        v
JWT Authentication Filter
```

The public endpoints are:

```text
POST /userRegistration
POST /login
```

All other application endpoints require authentication.

---

## 11. Stateless Session Management

The application uses:

```text
SessionCreationPolicy.STATELESS
```

This means the application does not maintain a server-side
HTTP session for authenticating users.

Each protected request must provide its authentication token.

The authentication model is therefore:

```text
Request 1 → JWT
Request 2 → JWT
Request 3 → JWT
...
```

The server does not rely on a persistent login session
between requests.

---

## 12. Public and Protected APIs

### Public APIs

The following APIs are publicly accessible:

```text
POST /userRegistration
POST /login
```

### Protected APIs

The remaining business APIs require authentication, including:

```text
POST /orders
GET  /orders
GET  /orders/{id}
GET  /orders/user/{userId}

GET  /trades
GET  /trades/{id}
```

---

## 13. Authorization Model

Version 1 uses authentication-based access control.

The primary rule is:

```text
Unauthenticated Request
        |
        v
Access Denied

Authenticated Request
        |
        v
Protected Business API
        |
        v
Access Allowed
```

Version 1 does not implement role-based authorization.

There are no separate user roles such as:

- ADMIN
- TRADER
- OPERATOR

All authenticated users currently operate under the same
authentication-based access model.

---

## 14. Request Validation

Authentication requests use request validation to reject
invalid input.

For example, the login request validates:

- Email format
- Non-blank email
- Non-blank password
- Password length constraints

Invalid request data is rejected before normal business
processing.

This helps prevent malformed authentication requests from
reaching the service layer.

---

## 15. Sensitive Data Protection

Sensitive information must not be exposed through application
logs or source control.

The following information should not be logged or committed:

- Plain-text passwords
- BCrypt password hashes
- JWT tokens
- Database passwords
- JWT secret keys
- Other authentication credentials

Entity `toString()` implementations should also avoid exposing
password fields or other sensitive authentication information.

---

## 16. JWT Expiration

JWTs contain an expiration timestamp.

During request authentication, the application validates the
token expiration.

An expired token is not considered valid for authentication.

The client must authenticate again to obtain a new token.

---

## 17. Password Verification

During login, password verification follows this flow:

```text
Login Password
      |
      v
PasswordEncoder.matches()
      |
      v
Stored BCrypt Hash
      |
      +---- Match ----> Authentication Successful
      |
      +---- No Match -> Invalid Credentials
```

The system does not compare the plain-text password directly
with the database value.

---

## 18. Security Error Handling

The application handles authentication-related failures
through application exception handling and Spring Security's
request authorization behavior.

Examples include:

- Invalid credentials
- User not found
- Invalid authentication token
- Expired JWT
- Unauthorized access to protected APIs
- Invalid request data

---

## 19. Version 1 Security Boundaries

The current security implementation does not include:

- Role-based authorization
- OAuth2 / OpenID Connect
- Refresh tokens
- Multi-factor authentication
- API key authentication
- Password reset workflow
- Account lockout
- Rate limiting
- Advanced audit logging
- Distributed authentication infrastructure

These capabilities are outside the scope of Version 1.

---

## 20. Security Design Goals

The Version 1 security design prioritizes:

- Secure password storage
- JWT-based authentication
- Stateless request authentication
- Protected business APIs
- Input validation
- Sensitive data protection
- Simple and maintainable security configuration

The design provides the authentication and API protection
required by the current Version 1 scope while leaving room
for stronger authorization and identity-management capabilities
in future versions.