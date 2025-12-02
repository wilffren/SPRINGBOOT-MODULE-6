# Task Management System - Hexagonal Architecture with Flyway Migrations

## Overview

This project demonstrates a professional task/event management system built with Spring Boot 3.5.1, implementing Hexagonal Architecture principles combined with database version control using Flyway migrations. The application manages Events, Venues, and Categories with complex Many-to-Many relationships, showcasing enterprise-grade database management and architectural patterns.

The system represents a significant evolution from basic CRUD operations to a production-ready application with controlled database schema evolution, optimized JPA configurations, and clean separation of concerns.

## Key Features

### Architectural Features
- **Hexagonal Architecture**: Clean separation between domain, application, and infrastructure layers
- **Domain-Driven Design**: Pure domain models without infrastructure dependencies
- **Flyway Migrations**: Version-controlled database schema evolution
- **Specification Pattern**: Dynamic, composable query building with Spring Data JPA

### Database Features
- **Managed Schema Evolution**: Incremental, version-controlled database changes
- **Complex Relationships**: Many-to-One and Many-to-Many with proper cascade handling
- **Optimized Indexes**: Strategic indexing for performance
- **Transaction Management**: Proper @Transactional configuration for read vs write operations
- **N+1 Query Prevention**: Hibernate statistics and batch fetching configuration

### Technical Features
- **JPA Entity Graphs**: Optimized lazy loading
- **Specification Queries**: Type-safe, composable filtering
- **Comprehensive Exception Handling**: Centralized error management
- **DTO Pattern**: Separation of API contracts from domain models
- **Lombok Integration**: Reduced boilerplate code

## Technology Stack

### Core Technologies
- **Java 21**: Latest LTS version with modern language features
- **Spring Boot 3.5.1**: Enterprise application framework
- **Maven**: Dependency management and build automation

### Database Layer
- **MySQL 8.0**: Relational database management system
- **Flyway 9.x**: Database migration and version control
- **Spring Data JPA**: Data access abstraction
- **Hibernate 6.x**: ORM implementation with advanced features

### Additional Dependencies
- **Jakarta Validation**: Bean validation framework
- **Lombok**: Code generation for DTOs and entities
- **Spring Boot DevTools**: Development utilities

## Architecture

### Hexagonal Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│               INFRASTRUCTURE LAYER                          │
│  ┌────────────────────────────────────────────────────────┐ │
│  │            REST Controllers                            │ │
│  │  EventController  │  VenueController                   │ │
│  │         ↓ DTOs (Request/Response)                      │ │
│  └────────────────────────────────────────────────────────┘ │
│  ┌────────────────────────────────────────────────────────┐ │
│  │         JPA Repositories & Entities                    │ │
│  │  JpaEventRepository │ Specifications                   │ │
│  │  EventEntity │ VenueEntity │ CategoryEntity            │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│              APPLICATION LAYER                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │         Services (Use Cases)                           │ │
│  │  EventService  │  VenueService                         │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                 DOMAIN LAYER                                │
│  ┌────────────────────────────────────────────────────────┐ │
│  │        Pure Domain Models (No JPA annotations)         │ │
│  │  Event  │  Venue  │  Category  │  EventStatus         │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### Project Structure

```
src/
├── main/
│   ├── java/com/example/HU4/
│   │   ├── domain/                    # DOMAIN LAYER
│   │   │   └── model/                 # Domain models (pure Java)
│   │   │       ├── Event.java
│   │   │       ├── Venue.java
│   │   │       ├── Category.java
│   │   │       └── EventStatus.java   # Enum
│   │   │
│   │   ├── application/               # APPLICATION LAYER
│   │   │   └── services/              # Use case implementations
│   │   │       ├── EventService.java
│   │   │       └── VenueService.java
│   │   │
│   │   ├── infrastructure/            # INFRASTRUCTURE LAYER
│   │   │   ├── controllers/           # REST API
│   │   │   │   ├── EventController.java
│   │   │   │   └── VenueController.java
│   │   │   ├── dto/                   # Data Transfer Objects
│   │   │   │   ├── EventRequest.java
│   │   │   │   ├── EventResponse.java
│   │   │   │   ├── EventFilterRequest.java
│   │   │   │   ├── VenueRequest.java
│   │   │   │   └── VenueResponse.java
│   │   │   ├── entities/              # JPA entities
│   │   │   │   ├── EventEntity.java
│   │   │   │   ├── VenueEntity.java
│   │   │   │   └── CategoryEntity.java
│   │   │   ├── repositories/          # Data access
│   │   │   │   ├── JpaEventRepository.java
│   │   │   │   ├── JpaVenueRepository.java
│   │   │   │   ├── JpaCategoryRepository.java
│   │   │   │   └── specifications/    # Query specifications
│   │   │   │       ├── EventSpecifications.java
│   │   │   │       └── VenueSpecifications.java
│   │   │   └── config/                # Configuration
│   │   │       ├── JpaConfig.java
│   │   │       └── GlobalExceptionHandler.java
│   │   │
│   │   └── Hu4Application.java        # Main application
│   │
│   └── resources/
│       ├── application.yaml           # Main configuration
│       └── db/migration/              # Flyway migrations
│           ├── V1__init_schema.sql
│           ├── V2__relaciones.sql
│           └── V3__ajustes.sql
│
└── test/
    └── java/com/example/HU4/
        └── Hu4ApplicationTests.java
```

## Database Schema

### Entity Relationship Diagram

```
┌──────────────────┐         ┌──────────────────┐
│     venues       │         │    categories    │
├──────────────────┤         ├──────────────────┤
│ id (PK)          │         │ id (PK)          │
│ name            │         │ name (UNIQUE)    │
│ location        │         │ description      │
│ capacity        │         │ created_at       │
│ description     │         │ updated_at       │
│ created_at      │         └──────────────────┘
│ updated_at      │                  ↑
└──────────────────┘                  │
         ↑                            │
         │ 1                          │
         │                            │
         │ N                          │ M
┌──────────────────┐         ┌──────────────────┐
│     events       │    N:M  │ event_categories │
├──────────────────┤─────────├──────────────────┤
│ id (PK)          │         │ event_id (PK,FK) │
│ name            │         │ category_id(PK,FK)│
│ description     │         └──────────────────┘
│ start_date      │
│ end_date        │
│ status          │
│ venue_id (FK)   │
│ created_at      │
│ updated_at      │
└──────────────────┘
```

### Table Definitions

#### venues
```sql
CREATE TABLE venues (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    location VARCHAR(300) NOT NULL,
    capacity INT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_venue_name (name),
    INDEX idx_venue_location (location),
    INDEX idx_venue_capacity (capacity)
);
```

#### events
```sql
CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    venue_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE CASCADE,
    INDEX idx_event_name (name),
    INDEX idx_event_start_date (start_date),
    INDEX idx_event_status (status),
    INDEX idx_event_dates (start_date, end_date),
    INDEX idx_event_venue_id (venue_id)
);
```

#### categories
```sql
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category_name (name)
);
```

#### event_categories (Join Table)
```sql
CREATE TABLE event_categories (
    event_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (event_id, category_id),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    INDEX idx_category_event (category_id, event_id)
);
```

## Flyway Migrations

### Migration Philosophy

Flyway enables version-controlled, incremental database changes that can be:
- Applied automatically on application startup
- Rolled out across multiple environments consistently
- Tracked and audited in the codebase
- Validated for integrity

### Migration Files

#### V1__init_schema.sql
Initial database structure:
- Creates venues, events, and categories tables
- Defines indexes for performance
- Inserts seed data for development

#### V2__relaciones.sql
Establishes relationships:
- Adds venue_id foreign key to events
- Creates event_categories join table for Many-to-Many
- Implements CASCADE constraints
- Adds composite indexes for query optimization
- Inserts sample events with relationships

#### V3__ajustes.sql
Schema refinements:
- Additional constraints or modifications
- Index optimizations
- Data updates or transformations

### Migration Naming Convention

Flyway follows the pattern: `V{VERSION}__{DESCRIPTION}.sql`
- **V**: Indicates a versioned migration
- **VERSION**: Sequential number (1, 2, 3, ...)
- **__**: Double underscore separator
- **DESCRIPTION**: Descriptive name in snake_case

## Domain Models

### Event (Pure Domain Model)

```java
public class Event {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventStatus status;
    private Long venueId;              // Reference, not object
    private List<Long> categoryIds;    // References to categories
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

**Key Points:**
- No JPA annotations
- Uses IDs for relationships (not objects)
- Framework-agnostic
- Can be tested in pure Java

### Venue (Pure Domain Model)

```java
public class Venue {
    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### Category (Pure Domain Model)

```java
public class Category {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### EventStatus (Enum)

```java
public enum EventStatus {
    ACTIVE,
    CANCELLED,
    POSTPONED,
    COMPLETED
}
```

## API Endpoints

### Events API

#### Create Event
```
POST /events
Content-Type: application/json

{
  "name": "Rock Concert 2025",
  "description": "Amazing rock concert",
  "startDate": "2025-12-01T20:00:00",
  "endDate": "2025-12-01T23:00:00",
  "status": "ACTIVE",
  "venueId": 1,
  "categoryIds": [1, 2]
}

Response: 201 Created
{
  "id": 1,
  "name": "Rock Concert 2025",
  "description": "Amazing rock concert",
  "startDate": "2025-12-01T20:00:00",
  "endDate": "2025-12-01T23:00:00",
  "status": "ACTIVE",
  "venue": {
    "id": 1,
    "name": "Teatro Nacional",
    "location": "San José, Costa Rica",
    "capacity": 1500,
    "description": "Principal teatro del país"
  },
  "categories": ["Música", "Concierto"],
  "createdAt": "2025-12-02T17:00:00",
  "updatedAt": "2025-12-02T17:00:00"
}
```

#### Get All Events
```
GET /events

Response: 200 OK
[
  {
    "id": 1,
    "name": "Rock Concert 2025",
    ...
  }
]
```

#### Get Event by ID
```
GET /events/{id}

Response: 200 OK
{
  "id": 1,
  "name": "Rock Concert 2025",
  ...
}
```

#### Search Events with Filters
```
POST /events/search
Content-Type: application/json

{
  "venueId": 1,
  "status": "ACTIVE",
  "startDateFrom": "2025-12-01T00:00:00",
  "endDateTo": "2025-12-31T23:59:59",
  "categoryName": "Música",
  "nameContains": "Concert"
}

Response: 200 OK
[
  { /* filtered events */ }
]
```

#### Get Events by Venue
```
GET /events/venue/{venueId}

Response: 200 OK
[
  { /* events at specified venue */ }
]
```

#### Update Event
```
PUT /events/{id}
Content-Type: application/json

{
  "name": "Updated Event Name",
  ...
}

Response: 200 OK
```

#### Delete Event
```
DELETE /events/{id}

Response: 204 No Content
```

### Venues API

#### Create Venue
```
POST /venues
Content-Type: application/json

{
  "name": "New Concert Hall",
  "location": "Downtown, City",
  "capacity": 2000,
  "description": "Modern concert venue"
}
```

#### Get All Venues
```
GET /venues
```

#### Get Venue by ID
```
GET /venues/{id}
```

#### Update Venue
```
PUT /venues/{id}
```

#### Delete Venue
```
DELETE /venues/{id}
```

## Configuration

### Application Configuration (application.yaml)

```yaml
spring:
  application:
    name: HU4-Task-Management
    
  datasource:
    url: jdbc:mysql://localhost:3306/hu4_tasks?useSSL=false&serverTimezone=UTC
    username: root
    password: Qwe.123*
    driver-class-name: com.mysql.cj.jdbc.Driver
    
  jpa:
    hibernate:
      ddl-auto: validate  # Flyway manages schema
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
        generate_statistics: true  # Detect N+1 queries
        default_batch_fetch_size: 10
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
    open-in-view: false  # Prevent lazy loading outside transactions
    
  flyway:
    enabled: true
    baseline-on-migrate: true
    locations: classpath:db/migration
    validate-on-migrate: true
```

### Key Configuration Highlights

**JPA Optimizations:**
- `ddl-auto: validate` - Hibernate validates schema but doesn't modify it (Flyway's job)
- `generate_statistics: true` - Enables N+1 detection
- `default_batch_fetch_size: 10` - Batch fetching for collections
- `open-in-view: false` - Forces explicit transaction boundaries

**Flyway Configuration:**
- `baseline-on-migrate: true` - Handles existing databases
- `validate-on-migrate: true` - Ensures migration integrity
- `locations: classpath:db/migration` - Where to find SQL files

## Advanced Features

### Specification Pattern for Dynamic Queries

The application uses Spring Data JPA Specifications for type-safe, composable queries:

```java
// EventSpecifications.java
public class EventSpecifications {
    
    public static Specification<EventEntity> hasVenue(Long venueId) {
        return (root, query, cb) -> 
            cb.equal(root.get("venue").get("id"), venueId);
    }
    
    public static Specification<EventEntity> hasStatus(EventStatus status) {
        return (root, query, cb) -> 
            cb.equal(root.get("status"), status);
    }
    
    public static Specification<EventEntity> nameContains(String name) {
        return (root, query, cb) -> 
            cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
    
    // And many more...
}
```

**Usage in Service:**
```java
Specification<EventEntity> spec = Specification.where(null);

if (filter.getVenueId() != null) {
    spec = spec.and(EventSpecifications.hasVenue(filter.getVenueId()));
}
if (filter.getStatus() != null) {
    spec = spec.and(EventSpecifications.hasStatus(filter.getStatus()));
}

List<EventEntity> results = eventRepository.findAll(spec);
```

### Transaction Management

Proper transaction annotation based on operation type:

```java
// Read operations
@Transactional(readOnly = true)
public EventResponse getEventById(Long id) {
    // Read-only transaction, optimizes performance
}

// Write operations
@Transactional
public EventResponse createEvent(EventRequest request) {
    // Read-write transaction, full ACID guarantees
}
```

### N+1 Query Prevention

Configuration to detect and optimize N+1 queries:

```yaml
jpa:
  properties:
    hibernate:
      generate_statistics: true
      default_batch_fetch_size: 10
```

**Hibernate Statistics in Logs:**
```
Hibernate: 
    select ... from events e
Hibernate: 
    select ... from venues v where v.id in (?, ?, ?)  -- Batch fetch
```

## Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git**

### Database Setup

1. Start MySQL server

2. Create database (or let Flyway create it):
```sql
CREATE DATABASE hu4_tasks;
```

3. Database will be automatically initialized by Flyway on first run

### Installation and Running

1. Clone and checkout HU-4 branch:
```bash
git clone <repository-url>
cd SPRINGBOOT-MODULE-6
git checkout HU-4
```

2. Build the project:
```bash
./mvnw clean install
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

4. The API will be available at: `http://localhost:8080`

### Verifying Flyway Migrations

On first startup, check logs for Flyway execution:

```
INFO o.f.core.internal.command.DbMigrate : Current version of schema `hu4_tasks`: << Empty Schema >>
INFO o.f.core.internal.command.DbMigrate : Migrating schema `hu4_tasks` to version "1 - init schema"
INFO o.f.core.internal.command.DbMigrate : Migrating schema `hu4_tasks` to version "2 - relaciones"
INFO o.f.core.internal.command.DbMigrate : Migrating schema `hu4_tasks` to version "3 - ajustes"
INFO o.f.core.internal.command.DbMigrate : Successfully applied 3 migrations
```

### Checking Flyway Status

Flyway creates a table `flyway_schema_history` to track migrations:

```sql
SELECT * FROM flyway_schema_history;
```

Output:
```
+----------------+---------+------------------+----------+
| installed_rank | version | description      | success  |
+----------------+---------+------------------+----------+
|              1 | 1       | init schema      |        1 |
|              2 | 2       | relaciones       |        1 |
|              3 | 3       | ajustes          |        1 |
+----------------+---------+------------------+----------+
```

## Testing the Application

### Create a Venue

```bash
curl -X POST http://localhost:8080/venues \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Modern Arena",
    "location": "City Center",
    "capacity": 5000,
    "description": "State-of-the-art venue"
  }'
```

### Create an Event

```bash
curl -X POST http://localhost:8080/events \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tech Conference 2025",
    "description": "Annual tech conference",
    "startDate": "2025-06-15T09:00:00",
    "endDate": "2025-06-17T18:00:00",
    "status": "ACTIVE",
    "venueId": 5,
    "categoryIds": [3]
  }'
```

### Search Events

```bash
curl -X POST http://localhost:8080/events/search \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ACTIVE",
    "startDateFrom": "2025-01-01T00:00:00",
    "categoryName": "Música"
  }'
```

## Flyway Best Practices

### 1. Never Modify Existing Migrations

Once a migration is applied, never modify it. Create a new migration instead:

```
❌ BAD: Editing V1__init_schema.sql after deployment
✅ GOOD: Creating V4__add_new_column.sql
```

### 2. Use Descriptive Names

```
❌ BAD: V1__update.sql
✅ GOOD: V1__add_email_column_to_users.sql
```

### 3. Test Migrations Locally First

Always test migrations in development before production:

```bash
./mvnw flyway:info      # Check migration status
./mvnw flyway:validate  # Validate migrations
./mvnw flyway:migrate   # Apply migrations
```

### 4. Include Rollback Strategy

For critical changes, document rollback procedures:

```sql
-- V5__add_archived_status.sql
ALTER TABLE events ADD COLUMN archived BOOLEAN DEFAULT FALSE;

-- To rollback (manual):
-- ALTER TABLE events DROP COLUMN archived;
```

### 5. Use Transactions

Flyway wraps each migration in a transaction by default, but be explicit:

```sql
-- V6__complex_data_migration.sql
START TRANSACTION;

-- Complex operations here

COMMIT;
```

## Benefits of This Architecture

### 1. Controlled Database Evolution

- All schema changes are version-controlled
- Consistent deployment across environments
- Audit trail of all database changes
- Easy rollback capabilities

### 2. Clean Architecture

- Domain layer is pure and testable
- Application layer orchestrates business logic
- Infrastructure is swappable

### 3. Type-Safe Queries

- Specifications provide compile-time safety
- Reduce runtime query errors
- Composable and reusable filters

### 4. Performance Optimization

- N+1 query detection
- Batch fetching configuration
- Strategic indexing
- Read-only transaction optimization

### 5. Maintainability

- Clear separation of concerns
- Explicit migration history
- Well-documented API
- Comprehensive error handling

## Common Issues and Solutions

### Flyway: Checksum Mismatch

**Error:** "Migration checksum mismatch"

**Cause:** Modified an already-applied migration

**Solution:**
```bash
# Option 1: Repair checksum (development only)
./mvnw flyway:repair

# Option 2: Clean and remigrate (DESTRUCTIVE - dev only)
./mvnw flyway:clean
./mvnw flyway:migrate
```

### JPA: LazyInitializationException

**Error:** "could not initialize proxy - no Session"

**Cause:** Accessing lazy-loaded data outside transaction

**Solution:**
```java
// Use @Transactional
@Transactional(readOnly = true)
public EventResponse getEvent(Long id) {
    EventEntity event = repository.findById(id);
    event.getCategories().size(); // Force initialization
    return toResponse(event);
}
```

### N+1 Query Problem

**Symptom:** Multiple individual SELECT queries

**Solution:**
```java
// Use JOIN FETCH in repository
@Query("SELECT e FROM EventEntity e LEFT JOIN FETCH e.categories WHERE e.id = :id")
EventEntity findByIdWithCategories(@Param("id") Long id);
```

## Future Enhancements

### Application Features
- **Pagination**: Add Page/Pageable support for large datasets
- **Search Optimization**: Full-text search with Hibernate Search
- **Event Tickets**: Ticket booking and inventory management
- **User Authentication**: Spring Security with JWT
- **File Upload**: Event images and attachments
- **Notifications**: Email/SMS reminders for events

### Database Enhancements
- **Audit Tables**: Track all data changes
- **Soft Deletes**: Mark records as deleted instead of removing
- **Partitioning**: Partition events table by date
- **Read Replicas**: Separate read/write databases

### Technical Improvements
- **Spring Security**: Authentication and authorization
- **Swagger/OpenAPI**: Interactive API documentation
- **Docker**: Containerization
- **Testcontainers**: Integration testing
- **Redis**: Caching layer
- **Kafka**: Event-driven architecture

## Development Workflow

### Adding a New Migration

1. Create new migration file:
```bash
touch src/main/resources/db/migration/V4__add_email_to_venues.sql
```

2. Write migration SQL:
```sql
-- V4__add_email_to_venues.sql
ALTER TABLE venues ADD COLUMN email VARCHAR(255);
CREATE INDEX idx_venue_email ON venues(email);
```

3. Restart application - Flyway applies automatically

4. Verify in logs:
```
INFO o.f.core.internal.command.DbMigrate : Migrating schema to version "4 - add email to venues"
INFO o.f.core.internal.command.DbMigrate : Successfully applied 1 migration
```

### Testing Strategy

**Unit Tests:** Test domain models in isolation
```java
@Test
void testEventCreation() {
    Event event = new Event("Test", "Description", start, end, venueId);
    assertEquals("Test", event.getName());
}
```

**Integration Tests:** Test with real database
```java
@SpringBootTest
@Transactional
class EventServiceIntegrationTest {
    @Autowired
    private EventService eventService;
    
    @Test
    void testCreateAndRetrieveEvent() {
        // Test with real database
    }
}
```

## Version Information

- **Application Version**: 1.0.0
- **Spring Boot**: 3.5.1
- **Java**: 21
- **Flyway**: 9.x (via Spring Boot)
- **Hibernate**: 6.x (via Spring Boot)
- **Build Tool**: Maven

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Resources

### Flyway
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Flyway Maven Plugin](https://flywaydb.org/documentation/usage/maven/)
- [Migration Best Practices](https://flywaydb.org/documentation/concepts/migrations#best-practices)

### Spring Data JPA
- [Spring Data JPA Specifications](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#specifications)
- [JPA Performance Tuning](https://vladmihalcea.com/tutorials/hibernate/)

### Hexagonal Architecture
- [Hexagonal Architecture Pattern](https://alistair.cockburn.us/hexagonal-architecture/)
- [DDD Patterns](https://martinfowler.com/tags/domain%20driven%20design.html)

---

This project demonstrates enterprise-grade Spring Boot development with database version control, clean architecture, and production-ready patterns.
