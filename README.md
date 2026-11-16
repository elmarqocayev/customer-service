# Customer Service Request Management REST API

REST API for customers and their support requests, built with **Java 21** and **Spring Boot 3.3.5**. Shows entity relationships, a request lifecycle with transition rules, and event-driven notifications.

## Features
- Customer CRUD with multiple addresses
- Support requests with priority (LOW, MEDIUM, HIGH, URGENT) and tags
- Pagination and filtering requests by status
- Status lifecycle with enforced transition rules
- Notifications sent asynchronously after a successful commit
- Bean Validation, custom exceptions, global exception handler

## Tech Stack
Java 21 · Spring Boot 3.3.5 · Spring Data JPA · Bean Validation · Lombok · H2 / PostgreSQL · JUnit 5 · Maven

## Run locally
```bash
mvn spring-boot:run                                        # dev profile: H2 in-memory (default)
mvn spring-boot:run -Dspring-boot.run.profiles=postgres    # PostgreSQL
mvn test
```
PostgreSQL settings can be overridden with `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.
H2 console: http://localhost:8080/h2-console

## API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/v1/customers` | Create a customer |
| GET    | `/api/v1/customers` | List customers (paginated) |
| GET    | `/api/v1/customers/{id}` | Get a customer |
| PUT    | `/api/v1/customers/{id}` | Update a customer |
| DELETE | `/api/v1/customers/{id}` | Delete a customer |
| POST   | `/api/v1/requests` | Create a support request |
| GET    | `/api/v1/requests?status=OPEN&page=0&size=10` | List requests (paginated, filter by status) |
| GET    | `/api/v1/requests/{id}` | Get a request |
| PATCH  | `/api/v1/requests/{id}/status` | Change request status |

## Request lifecycle
```
OPEN → IN_PROGRESS → RESOLVED → CLOSED
```
| From | Allowed next status |
|------|---------------------|
| OPEN | IN_PROGRESS, CLOSED |
| IN_PROGRESS | RESOLVED, OPEN |
| RESOLVED | CLOSED, IN_PROGRESS |
| CLOSED | none |

An invalid transition returns `422` and the transaction is rolled back.

## Error handling
| Case | HTTP status |
|------|-------------|
| Resource not found | 404 |
| Duplicate resource | 409 |
| Business rule violation (invalid status change) | 422 |
| Validation errors, malformed JSON / enum | 400 |

## Design notes
- **Relationships:** Customer → Address (One-to-Many, cascade + orphan removal); ServiceRequest ↔ Tag (Many-to-Many); ServiceRequest → Customer (Many-to-One, lazy).
- **Transactions:** service methods are `@Transactional`; a rule violation throws and rolls back the change.
- **Events:** a status change publishes an event handled by `@TransactionalEventListener(AFTER_COMMIT)`, so notifications are never sent for rolled-back changes.
- **Async:** the listener runs with `@Async` on a dedicated thread pool (`notificationExecutor`, 2–4 threads).
- **Tests:** JUnit 5 tests for status transition rules.
