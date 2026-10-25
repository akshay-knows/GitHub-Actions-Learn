# SwiftyQuote Simple Microservices — API List

## Start (local development)
- Java 17, Maven, Docker.
- Run `docker compose up -d kafka` for Kafka. MySQL is optional; services use in-memory H2 by default.
- Start applications in order: discovery-server (8761), admin-server (9090), qms-service (8081), workflow-service (8082), notification-service (8083).
- From the root: `mvn clean package -DskipTests` and then `java -jar <module>/target/<module>-1.0.0.jar` for each service in separate terminals. Or run the five `Application` classes in the IDE.
- The H2 database resets whenever a service restarts.
- Admin UI: http://localhost:9090 ; Eureka UI: http://localhost:8761 . No login — local learning only.
- Business call flow: create quotation, submit for approval (Feign), approve/reject in Workflow, Kafka notification appears in notification list.
- Swagger, Gateway, Security, Redis, AWS, tests deliberately excluded.
- These are minimal educational implementations; no authentication or reliable distributed transactions. Do not expose them to the Internet.

## QMS — port 8081
| Method | Path | Purpose |
|---|---|---|
| POST | `/api/quotations` | Create quotation with multiple line items; calculate total |
| GET | `/api/quotations?page=0&size=10` | Paginated quotations sorted newest first |
| GET | `/api/quotations/{id}` | View quotation summary |
| GET | `/api/quotations/{id}/preview` | **Unique:** itemized quotation preview (product, qty, line total) |
| POST | `/api/quotations/{id}/submit-approval` | Submit draft quotation to Workflow via OpenFeign |
| POST | `/api/quotations/{id}/decision` | Internal Workflow callback: `{"status":"APPROVED"}` or `{"status":"REJECTED"}` |

## Workflow — port 8082
| Method | Path | Purpose |
|---|---|---|
| POST | `/api/approvals` | Called by QMS to create a manager approval request |
| GET | `/api/approvals/{id}` | See approval status |
| POST | `/api/approvals/{id}/approve` | Manager approves; calls QMS and publishes Kafka event |
| POST | `/api/approvals/{id}/reject` | **Unique:** manager rejection reason required; calls QMS and publishes Kafka event |

## Notification — port 8083
| Method | Path | Purpose |
|---|---|---|
| GET | `/api/notifications?page=0&size=10` | See Kafka-consumed mock email history |

## Monitoring
| URL | Purpose |
|---|---|
| http://localhost:8761 | Eureka UI |
| http://localhost:9090 | Spring Boot Admin UI |
| http://localhost:8081/actuator/health | QMS health |
| http://localhost:8082/actuator/health | Workflow health |
| http://localhost:8083/actuator/health | Notification health |

## Logging
The services use SLF4J INFO logs for creation, submission, decisions and mock-email delivery. WARNING/ERROR logs show request failures and Feign/callback errors. Read the service console or Admin Server loggers/health view.

## MySQL optional
For practice, use the default in-memory H2 first. To switch a service to MySQL, create the database manually and set `DB_URL` (e.g., `jdbc:mysql://localhost:3307/qms_db?createDatabaseIfNotExist=true`), `DB_USERNAME=root`, `DB_PASSWORD=localroot` in its run configuration. Keep one schema per service.
