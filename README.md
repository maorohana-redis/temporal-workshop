# 🕒 Order Fulfillment Workshop (Temporal Java + Spring Boot)

This is a hands-on workshop to learn Temporal Java SDK using a real-world style order fulfillment system. You'll explore:
- Workflows & Activities
- Signals & QueryMethods
- Spring Boot integration
- Determinism testing via Replay API
- Temporal Web UI + CLI interaction

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17+
- Gradle 7+
- [Homebrew](https://brew.sh) (for macOS)
- Docker (for running Temporal locally)

---

### ⚙️ Step 1: Install Temporal Server

Using [Temporal CLI](https://docs.temporal.io/cli):

```bash
brew install temporal
temporal server start-dev --db-filename temporal.db
```

> This spins up a lightweight Temporal server with Web UI at [http://localhost:8233](http://localhost:8233)

---

### ⚙️ Step 2: Run the Worker & Spring Boot API

1Start the worker and API:

```bash
./gradlew bootRun
```

> Worker listens to `order-queue` and exposes APIs on `http://localhost:8080`

---

### 📦 REST API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET`  | `/orders/in-process` | List all active workflow IDs |
| `POST` | `/orders/cancel/{workflowId}` | Send cancel signal to workflow |

---

### 🧪 Run Tests

```bash
./gradlew test
```

Tests include:
- A standard unit test that runs the workflow

---
