# Distributed Ticket Booking System

A high-precision online ticket booking system designed using **Domain-Driven Design (DDD)** principles and built on **Java Spring Boot**. The architecture is specifically optimized for **High Concurrency** and engineered to completely eliminate **Race Conditions** (the Overbooking problem) during flash-sale events with massive traffic spikes.

---

## Architecture & Tech Stack

### 1. Design Principles & Core Domain
* **Domain-Driven Design (DDD):** Clean separation of concerns into *Domain, Application, Infrastructure, and Controller (API)* layers.

### 2. High-Concurrency & Race Condition Mitigation
* **Distributed Locks (Redisson):** Leverages Redis-based Distributed Locks to synchronize ticket deduction tasks (`Inventory Decrement`) across a multi-instance/multi-node environment. This prevents simultaneous write conflicts at the database level.
* **Multi-level Caching Strategy:**
    * **L1 Cache (Guava Cache):** An in-memory JVM local cache for ultra-high-frequency, read-heavy data that changes infrequently (e.g., event details, raw seat layouts). Response times are kept within microseconds ($\mu s$).
    * **L2 Cache (Redis Cluster):** A distributed cache managing real-time available ticket counts. Incoming availability checks are intercepted and absorbed here before hitting the database.

### 3. Fault Tolerance & Observability
* **Resilience4j:** Implements design patterns to isolate failures:
    * *Circuit Breaker:* Trips when 3rd-party payment gateways or databases become unresponsive.
    * *Rate Limiter:* Throttles request rates per User/IP to prevent automation scripts and DDoS attacks.=
* **Distributed Logging (ELK Stack):** Logback integrated attaches a unique `ticketId` to every flow, aggregated seamlessly into Elasticsearch, Logstash, and Kibana.
* **Metrics & Visualization:** **Prometheus** scrapes operational metrics (JVM, HikariCP Connection Pools, Redisson Lock metrics), visualized through real-time **Grafana** dashboards.

---

## 📁 Project Directory Structure (DDD Layout)

The project adheres to a strict DDD Layered Architecture to isolate the Domain Layer from external frameworks: