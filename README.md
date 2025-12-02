# Event Catalog Backend - Sistema de Gestión de Eventos

## Overview

Event Catalog Backend es una aplicación empresarial desarrollada con Spring Boot 3.5.6 que proporciona una solución completa para la gestión de eventos y recintos. El sistema implementa persistencia con JPA/Hibernate en MySQL, ofrece una API RESTful robusta y cuenta con una interfaz web construida con Thymeleaf para la visualización y administración de eventos.

## Features

### Core Functionality
- **Event Management**: CRUD completo para la gestión de eventos con validaciones de negocio
- **Venue Management**: Administración de recintos/lugares donde se realizan eventos
- **Advanced Filtering**: Sistema de filtrado dinámico por ciudad, categoría, fecha y precio
- **Pagination Support**: Paginación y ordenamiento de resultados para optimizar rendimiento
- **Data Validation**: Validación de duplicados, integridad referencial y reglas de negocio
- **Web Interface**: Interfaz web responsive con Thymeleaf y Bootstrap 5

### Technical Features
- **JPA/Hibernate**: Persistencia con relaciones bidireccionales y optimización de consultas
- **Specifications Pattern**: Filtrado dinámico y combinable usando Spring Data JPA Specifications
- **Exception Handling**: Manejo centralizado de excepciones con respuestas HTTP estandarizadas
- **Database Auditing**: Timestamps automáticos (createdAt, updatedAt) con JPA Auditing
- **Connection Pooling**: HikariCP para gestión eficiente de conexiones a base de datos
- **H2 Support**: Base de datos en memoria opcional para desarrollo y testing

## Technical Stack

### Core Technologies
- **Java 21**: Última versión LTS con características modernas del lenguaje
- **Spring Boot 3.5.6**: Framework empresarial para aplicaciones Java
- **Maven**: Gestión de dependencias y automatización de builds

### Persistence Layer
- **Spring Data JPA**: Capa de abstracción para acceso a datos
- **Hibernate**: Implementación de JPA para mapeo objeto-relacional
- **MySQL 8.0**: Sistema de gestión de base de datos relacional
- **H2 Database**: Base de datos en memoria para desarrollo (opcional)
- **HikariCP**: Pool de conexiones de alto rendimiento

### Presentation Layer
- **Thymeleaf**: Motor de plantillas para renderizado server-side
- **Thymeleaf Layout Dialect**: Sistema de layouts reutilizables
- **Bootstrap 5.3.0**: Framework CSS para diseño responsive
- **jQuery 3.7.1**: Biblioteca JavaScript para manipulación DOM
- **WebJars**: Gestión de recursos frontend vía Maven

### Development Tools
- **Lombok**: Reducción de código boilerplate
- **Spring DevTools**: Hot reload para desarrollo ágil
- **Jakarta Validation**: Validación declarativa de beans

## Architecture

La aplicación sigue una arquitectura en capas (Layered Architecture) con separación clara de responsabilidades, implementando principios SOLID:

### Layer Structure

```
Presentation Layer (Controllers)
    ↓
Business Logic Layer (Services)
    ↓
Data Access Layer (Repositories)
    ↓
Persistence Layer (Entities)
```

### Components

#### 1. Entity Layer
Define el modelo de dominio con anotaciones JPA:

- **EventEntity**: Representa eventos con relación ManyToOne a VenueEntity
  - Propiedades: id, name, description, category, city, eventDate, price, availableTickets, imageUrl, status
  - Relaciones: @ManyToOne con VenueEntity (LAZY loading)
  - Auditoría: createdAt, updatedAt (automático)
  - Constraints: nombre único, índices en category, eventDate, city

- **VenueEntity**: Representa recintos/lugares con relación OneToMany a eventos
  - Propiedades: id, name, address, city, capacity, description
  - Relaciones: @OneToMany con EventEntity (bidireccional, CASCADE ALL)
  - Auditoría: createdAt, updatedAt (automático)
  - Constraints: nombre único, índices en name, city

#### 2. Repository Layer
Interfaces Spring Data JPA con métodos de consulta personalizados:

- **EventRepository**: Consultas especializadas para eventos
  - Métodos derivados: findByCity, findByCategory, findByEventDateAfter
  - Soporte para Specifications (filtrado dinámico)
  - Paginación y ordenamiento

- **VenueRepository**: Acceso a datos de recintos
  - Consultas por ciudad y capacidad
  - Verificación de existencia por nombre

#### 3. Service Layer
Lógica de negocio y orquestación de operaciones:

- **EventServiceImpl**: Gestión completa de eventos
  - Validación de duplicados por nombre
  - Verificación de integridad referencial con venues
  - Filtrado avanzado con Specifications
  - Paginación y ordenamiento
  - Conversión entre DTOs y entidades

- **VenueServiceImpl**: Gestión de recintos
  - Validación de unicidad de nombres
  - Prevención de eliminación si tiene eventos asociados
  - Gestión de relaciones bidireccionales

#### 4. Controller Layer

**REST API Controllers:**
- **EventController**: Endpoints REST para eventos
- **VenueController**: Endpoints REST para venues

**Web Controllers (Thymeleaf):**
- **HomeController**: Página principal
- **WebEventController**: Vista de eventos (lista, detalle, formulario)
- **WebVenueController**: Vista de venues

#### 5. DTO Layer
Data Transfer Objects para comunicación API:

- **EventRequestDTO**: Datos de entrada para crear/actualizar eventos
- **EventResponseDTO**: Respuesta con información completa del evento
- **VenueRequestDTO**: Datos de entrada para venues
- **VenueResponseDTO**: Respuesta con información del venue
- **EventFilterDTO**: Criterios de filtrado (ciudad, categoría, fechas, precios)
- **PageResponseDTO**: Respuesta paginada genérica

#### 6. Specification Layer
Filtrado dinámico con JPA Criteria API:

- **EventSpecification**: Construcción de consultas dinámicas
  - Filtros combinables por ciudad, categoría, fecha, precio
  - Filtros predefinidos: eventos futuros, activos, con tickets disponibles
  - Composición de filtros con operadores AND

#### 7. Exception Layer
Manejo centralizado de excepciones:

- **GlobalExceptionHandler**: @RestControllerAdvice para manejo global
- Excepciones específicas:
  - EventNotFoundException
  - VenueNotFoundException
  - DuplicateEventException
  - DuplicateVenueException
  - ResourceInUseException
  - InvalidFilterException
  - InvalidPaginationException
  - BusinessLogicException

### SOLID Principles Implementation

- **Single Responsibility**: Cada clase tiene una única responsabilidad claramente definida
- **Open/Closed**: Extensible mediante interfaces y herencia, cerrado a modificaciones
- **Liskov Substitution**: Las implementaciones pueden sustituir sus interfaces sin problemas
- **Interface Segregation**: Interfaces específicas y enfocadas (IEventService, IVenueService)
- **Dependency Inversion**: Dependencias en abstracciones (interfaces) no en implementaciones

## Data Model

### Event Entity

```
events
├── id (BIGINT, PK, AUTO_INCREMENT)
├── name (VARCHAR(150), UNIQUE, NOT NULL) ← Índice
├── description (VARCHAR(1000), NOT NULL)
├── category (VARCHAR(50), NOT NULL) ← Índice
├── city (VARCHAR(50), NOT NULL) ← Índice
├── event_date (DATETIME, NOT NULL) ← Índice
├── price (DECIMAL(10,2), NOT NULL)
├── available_tickets (INT, NOT NULL)
├── image_url (VARCHAR(500))
├── venue_id (BIGINT, FK → venues.id, NULLABLE)
├── status (ENUM: ACTIVE, CANCELLED, COMPLETED, POSTPONED)
├── created_at (DATETIME, NOT NULL)
└── updated_at (DATETIME)
```

### Venue Entity

```
venues
├── id (BIGINT, PK, AUTO_INCREMENT)
├── name (VARCHAR(100), UNIQUE, NOT NULL) ← Índice
├── address (VARCHAR(200), NOT NULL)
├── city (VARCHAR(50), NOT NULL) ← Índice
├── capacity (INT, NOT NULL)
├── description (VARCHAR(500))
├── created_at (DATETIME, NOT NULL)
└── updated_at (DATETIME)
```

### Relationships

- **Event → Venue**: ManyToOne (muchos eventos pueden realizarse en un mismo venue)
- **Venue → Events**: OneToMany (un venue puede tener múltiples eventos)
- Relación bidireccional con CASCADE ALL y orphanRemoval en lado OneToMany
- LAZY loading para optimizar consultas

## API Endpoints

### Events API

#### Create Event
```
POST /api/events
Content-Type: application/json

{
  "name": "Concierto Rock 2025",
  "description": "El mejor concierto del año",
  "category": "Música",
  "city": "Bogotá",
  "eventDate": "2025-06-15T20:00:00",
  "price": 150000.00,
  "availableTickets": 500,
  "imageUrl": "https://example.com/image.jpg",
  "venueId": 1
}
```

#### Get All Events (Paginated)
```
GET /api/events?page=0&size=10&sort=eventDate,asc
```

#### Get Events with Filters
```
GET /api/events?page=0&size=10&city=Bogotá&category=Música&minPrice=50000&maxPrice=200000
```

#### Get Event by ID
```
GET /api/events/{id}
```

#### Get Events by City
```
GET /api/events/city/{city}
```

#### Get Events by Category
```
GET /api/events/category/{category}
```

#### Get Upcoming Events
```
GET /api/events/upcoming
```

#### Update Event
```
PUT /api/events/{id}
Content-Type: application/json
```

#### Delete Event
```
DELETE /api/events/{id}
```

### Venues API

#### Create Venue
```
POST /api/venues
Content-Type: application/json

{
  "name": "Estadio El Campín",
  "address": "Carrera 30 # 57-60",
  "city": "Bogotá",
  "capacity": 36000,
  "description": "Estadio multiuso"
}
```

#### Get All Venues
```
GET /api/venues
```

#### Get Venue by ID
```
GET /api/venues/{id}
```

#### Get Venues by City
```
GET /api/venues/city/{city}
```

#### Update Venue
```
PUT /api/venues/{id}
```

#### Delete Venue
```
DELETE /api/venues/{id}
```

## Configuration

### Database Configuration

MySQL connection settings in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/event_catalog_db?createDatabaseIfNotExist=true
    username: root
    password: Qwe.123*
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
```

### JPA/Hibernate Configuration

```yaml
spring:
  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
    hibernate:
      ddl-auto: update  # create, create-drop, update, validate, none
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
```

### Server Configuration

```yaml
server:
  port: 8080
  servlet:
    context-path: /api
```

### Thymeleaf Configuration

```yaml
thymeleaf:
  cache: false  # Disabled in development
  encoding: UTF-8
  mode: HTML
  prefix: classpath:/templates/
  suffix: .html
```

## Getting Started

### Prerequisites

- **Java Development Kit (JDK) 21** or higher
- **Maven 3.6+** for dependency management
- **MySQL 8.0+** database server running
- **Git** for version control

### Database Setup

1. Install and start MySQL server

2. Create database (automatic with `createDatabaseIfNotExist=true`):
```sql
CREATE DATABASE event_catalog_db;
```

3. Configure credentials in `application.yml` or use environment variables:
```bash
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

### Installation and Execution

1. Clone the repository:
```bash
git clone <repository-url>
cd SPRINGBOOT-MODULE-6
git checkout HU-2
```

2. Build the project:
```bash
./mvnw clean install
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

4. Alternative: Run compiled JAR:
```bash
./mvnw clean package
java -jar target/event-catalog-backend-1.0.0.jar
```

### Accessing the Application

Once started, the application is available at:

- **API Base URL**: http://localhost:8080/api
- **Web Interface**: http://localhost:8080/api (home page)
- **Events List**: http://localhost:8080/api/web/events
- **Create Event**: http://localhost:8080/api/web/events/new

The console displays a startup message with all available endpoints.

## Web Interface

The application includes a complete web interface built with Thymeleaf:

### Pages

- **Home** (`/api`): Landing page with navigation
- **Event List** (`/api/web/events`): Paginated list of all events
- **Event Detail** (`/api/web/events/{id}`): Detailed view with venue information
- **Create Event** (`/api/web/events/new`): Form to create new events
- **Edit Event** (`/api/web/events/edit/{id}`): Form to update events

### Features

- Responsive design with Bootstrap 5
- Client-side form validation
- Dynamic filtering and search
- Pagination controls
- Success/error notifications

## Error Handling

The application provides standardized error responses:

```json
{
  "timestamp": "2025-12-02T17:50:00",
  "status": 404,
  "error": "Not Found",
  "message": "Event not found with ID: 999",
  "path": "/api/events/999"
}
```

### HTTP Status Codes

- **200 OK**: Successful GET/PUT operation
- **201 Created**: Resource created successfully
- **204 No Content**: Successful DELETE operation
- **400 Bad Request**: Validation error or invalid parameters
- **404 Not Found**: Resource not found
- **409 Conflict**: Duplicate resource (e.g., event name already exists)
- **422 Unprocessable Entity**: Business logic violation
- **500 Internal Server Error**: Unexpected server error

## Advanced Features

### Dynamic Filtering with Specifications

The application uses Spring Data JPA Specifications for flexible, composable queries:

```java
// Example: Filter events by multiple criteria
EventFilterDTO filters = EventFilterDTO.builder()
    .city("Bogotá")
    .category("Música")
    .minPrice(50000.0)
    .maxPrice(200000.0)
    .startDate(LocalDateTime.now())
    .build();

Page<EventResponseDTO> events = eventService.findWithFilters(filters, pageable);
```

### Pagination and Sorting

Support for pagination and multi-field sorting:

```
GET /api/events?page=0&size=20&sort=eventDate,asc&sort=price,desc
```

### Audit Trail

Automatic timestamps on all entities:
- `createdAt`: Set automatically on entity creation
- `updatedAt`: Updated automatically on entity modification

Enabled via `@EnableJpaAuditing` and Hibernate annotations.

## Development

### Running Tests

```bash
./mvnw test
```

### Development Mode with Hot Reload

Spring Boot DevTools enables automatic restart on code changes:

```bash
./mvnw spring-boot:run
```

### Building for Production

```bash
./mvnw clean package -DskipTests
java -jar target/event-catalog-backend-1.0.0.jar --spring.profiles.active=prod
```

### Database Migrations

For production, consider using Flyway or Liquibase for version-controlled database migrations. Currently, the application uses `hibernate.ddl-auto=update`.

## Project Structure

```
src/
├── main/
│   ├── java/com/eventcatalog/
│   │   ├── config/                    # Configuration classes
│   │   │   ├── CorsConfig.java
│   │   │   └── (OpenApiConfig.java - if added)
│   │   ├── controller/                # REST API controllers
│   │   │   ├── EventController.java
│   │   │   ├── VenueController.java
│   │   │   └── web/                   # Web controllers
│   │   │       ├── HomeController.java
│   │   │       ├── WebEventController.java
│   │   │       └── WebVenueController.java
│   │   ├── dto/                       # Data Transfer Objects
│   │   │   ├── EventRequestDTO.java
│   │   │   ├── EventResponseDTO.java
│   │   │   ├── EventFilterDTO.java
│   │   │   ├── VenueRequestDTO.java
│   │   │   ├── VenueResponseDTO.java
│   │   │   └── PageResponseDTO.java
│   │   ├── entity/                    # JPA entities
│   │   │   ├── EventEntity.java
│   │   │   └── VenueEntity.java
│   │   ├── exception/                 # Exception handling
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── EventNotFoundException.java
│   │   │   ├── VenueNotFoundException.java
│   │   │   ├── DuplicateEventException.java
│   │   │   ├── DuplicateVenueException.java
│   │   │   ├── ResourceInUseException.java
│   │   │   ├── InvalidFilterException.java
│   │   │   ├── InvalidPaginationException.java
│   │   │   ├── BusinessLogicException.java
│   │   │   └── ErrorResponse.java
│   │   ├── repository/                # Data access layer
│   │   │   ├── EventRepository.java
│   │   │   └── VenueRepository.java
│   │   ├── service/                   # Business logic
│   │   │   ├── IEventService.java
│   │   │   ├── EventServiceImpl.java
│   │   │   ├── IVenueService.java
│   │   │   └── VenueServiceImpl.java
│   │   ├── specification/             # JPA Specifications
│   │   │   └── EventSpecification.java
│   │   └── EventCatalogApplication.java
│   └── resources/
│       ├── application.yml            # Main configuration
│       ├── application-dev.yml        # Development profile
│       ├── static/                    # Static resources
│       └── templates/                 # Thymeleaf templates
│           ├── layout.html            # Base layout
│           ├── home.html              # Home page
│           └── events/                # Event templates
│               ├── list.html
│               ├── detail.html
│               └── form.html
└── test/
    └── java/com/eventcatalog/
        └── ApplicationTests.java
```

## Logging

Comprehensive logging with SLF4J and Logback:

```yaml
logging:
  level:
    root: INFO
    com.eventcatalog: DEBUG
    org.springframework.web: INFO
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
  file:
    name: logs/event-catalog.log
```

Logs include:
- SQL queries (formatted)
- Parameter binding
- Transaction management
- Exception stack traces
- Business operation audit trail

## Future Enhancements

- **Spring Security**: Authentication and authorization
- **JWT Tokens**: Stateless API authentication
- **Swagger/OpenAPI**: Interactive API documentation
- **Flyway/Liquibase**: Database version control and migrations
- **Redis Cache**: Response caching for improved performance
- **Elasticsearch**: Full-text search for events
- **File Upload**: Event image management with cloud storage
- **Email Notifications**: Event reminders and updates
- **Payment Integration**: Ticket purchase system
- **Analytics Dashboard**: Event statistics and reporting
- **Multi-language Support**: i18n for international events
- **GraphQL API**: Alternative query interface

## Version Information

- **Application Version**: 1.0.0
- **Spring Boot**: 3.5.6
- **Java**: 21
- **Build Tool**: Maven
- **Database**: MySQL 8.0

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Author

**Event Catalog Team**

---

For questions, issues, or contributions, please refer to the project repository and issue tracker.
