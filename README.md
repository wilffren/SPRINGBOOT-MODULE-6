# Ticketing API - Event and Venue Management System

## Overview

Ticketing API is a RESTful web service built with Spring Boot 3.5.6 for managing events and venues. This application provides a comprehensive catalog system for event ticketing, featuring in-memory data storage with a clean, layered architecture following SOLID principles.

## Features

- **Event Management**: Full CRUD operations for creating, reading, updating, and deleting events
- **Venue Management**: Complete venue catalog with location and capacity information
- **RESTful API**: Well-designed REST endpoints following HTTP best practices
- **Interactive API Documentation**: Integrated Swagger/OpenAPI UI for easy API exploration and testing
- **Data Validation**: Comprehensive input validation using Jakarta Bean Validation
- **Exception Handling**: Centralized error handling with meaningful HTTP responses
- **In-Memory Storage**: Thread-safe concurrent data storage for development and testing
- **CORS Support**: Configured for cross-origin resource sharing

## Technical Stack

### Core Technologies
- **Java 21**: Latest LTS version with modern language features
- **Spring Boot 3.5.6**: Enterprise-grade application framework
- **Maven**: Dependency management and build automation

### Key Dependencies
- **Spring Web**: RESTful web services and MVC support
- **Spring Data JPA**: Data persistence layer (MySQL-ready)
- **SpringDoc OpenAPI 2.7.0**: Swagger UI and API documentation
- **Lombok**: Reduce boilerplate code with annotations
- **Jakarta Validation**: Bean validation for DTOs
- **MySQL Connector**: Database driver (future integration)

## Architecture

The application follows a layered architecture pattern implementing SOLID principles:

### Layers
1. **Controller Layer**: HTTP request handling and response mapping
   - `EventController`: Event management endpoints
   - `VenueController`: Venue management endpoints

2. **Service Layer**: Business logic and orchestration
   - `IEventService` / `EventServiceImpl`: Event business operations
   - `IVenueService` / `VenueServiceImpl`: Venue business operations

3. **Repository Layer**: Data access and persistence
   - `IEventRepository` / `EventRepositoryImpl`: Event data management
   - `IVenueRepository` / `VenueRepositoryImpl`: Venue data management

4. **DTO Layer**: Data Transfer Objects for API communication
   - `EventDTO`: Event data representation
   - `VenueDTO`: Venue data representation
   - `ErrorResponseDTO`: Standardized error responses

### Design Principles
- **Single Responsibility Principle**: Each class has a single, well-defined purpose
- **Dependency Inversion**: Dependencies on abstractions rather than concrete implementations
- **Open/Closed Principle**: Open for extension, closed for modification
- **Liskov Substitution**: Repository implementations are interchangeable
- **Interface Segregation**: Focused, role-specific interfaces

## Data Models

### Event
- **id**: Unique identifier (Long)
- **name**: Event name (3-100 characters, required)
- **description**: Event details (max 500 characters)
- **eventDate**: Date and time of the event (required)
- **venueId**: Associated venue identifier (required)
- **capacity**: Maximum attendance (required, positive)
- **price**: Ticket price (required, positive)
- **category**: Event type (required)
- **active**: Active status flag

### Venue
- **id**: Unique identifier (Long)
- **name**: Venue name (3-100 characters, required)
- **address**: Physical address (max 200 characters, required)
- **city**: City location (required)
- **country**: Country location (required)
- **capacity**: Maximum capacity (required, positive)
- **type**: Venue type (stadium, theater, arena, etc., required)
- **contactInfo**: Contact information
- **active**: Active status flag

## API Endpoints

### Events
- `POST /api/events` - Create a new event
- `GET /api/events` - Retrieve all events
- `GET /api/events/{id}` - Retrieve a specific event
- `PUT /api/events/{id}` - Update an existing event
- `DELETE /api/events/{id}` - Delete an event

### Venues
- `POST /api/venues` - Create a new venue
- `GET /api/venues` - Retrieve all venues
- `GET /api/venues/{id}` - Retrieve a specific venue
- `PUT /api/venues/{id}` - Update an existing venue
- `DELETE /api/venues/{id}` - Delete a venue

## Configuration

### Application Properties

The application uses YAML configuration with the following key settings:

- **Server Port**: 8080
- **Context Path**: /api
- **Database**: MySQL (prepared for future integration)
- **Active Profile**: dev
- **Swagger UI**: /api/swagger-ui.html
- **API Docs**: /api/v3/api-docs

### Database Configuration

The application is configured for MySQL but currently uses in-memory storage. Database settings:
- **URL**: `jdbc:mysql://localhost:3306/ticketing_db`
- **Username**: root (configurable via `DB_USERNAME` env variable)
- **Password**: Qwe.123* (configurable via `DB_PASSWORD` env variable)
- **JPA DDL**: auto-update (creates/updates tables automatically)

## Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher (for future database integration)

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd SPRINGBOOT-MODULE-6
```

2. Build the project:
```bash
./mvnw clean install
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

### Accessing the Application

Once started, the application will be available at:

- **API Base URL**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI Docs**: http://localhost:8080/api/v3/api-docs

The console will display a startup message with these URLs for easy access.

## API Documentation

The application includes comprehensive API documentation via Swagger UI. Access the interactive documentation at:

**http://localhost:8080/api/swagger-ui.html**

Features:
- Complete endpoint documentation
- Try-it-out functionality for testing
- Request/response schemas
- Validation requirements
- HTTP status codes and error responses

## Error Handling

The application provides standardized error responses with the following structure:

```json
{
  "timestamp": "2025-12-02T17:46:56",
  "status": 404,
  "error": "Not Found",
  "message": "Event not found with ID: 1",
  "path": "/api/events/1",
  "validationErrors": []
}
```

### HTTP Status Codes
- **200 OK**: Successful GET/PUT request
- **201 Created**: Successful POST request
- **204 No Content**: Successful DELETE request
- **400 Bad Request**: Validation error or malformed request
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Unexpected server error

## Development

### Running Tests
```bash
./mvnw test
```

### Development Mode
The application includes Spring Boot DevTools for hot reload during development:
```bash
./mvnw spring-boot:run
```

### Building for Production
```bash
./mvnw clean package
java -jar target/ticketing-api-1.0.0-SNAPSHOT.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/com/ticketing/
│   │   ├── config/          # Configuration classes
│   │   │   ├── CorsConfig.java
│   │   │   └── OpenApiConfig.java
│   │   ├── controller/      # REST controllers
│   │   │   ├── EventController.java
│   │   │   └── VenueController.java
│   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── EventDTO.java
│   │   │   ├── VenueDTO.java
│   │   │   └── ErrorResponseDTO.java
│   │   ├── exception/       # Exception handling
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── ResourceNotFoundException.java
│   │   ├── repository/      # Data access layer
│   │   │   ├── IEventRepository.java
│   │   │   ├── EventRepositoryImpl.java
│   │   │   ├── IVenueRepository.java
│   │   │   └── VenueRepositoryImpl.java
│   │   ├── service/         # Business logic layer
│   │   │   ├── IEventService.java
│   │   │   ├── EventServiceImpl.java
│   │   │   ├── IVenueService.java
│   │   │   └── VenueServiceImpl.java
│   │   └── TicketingApplication.java
│   └── resources/
│       ├── application.yml
│       ├── application-dev.yml
│       └── application.properties
└── test/
    └── java/com/ticketing/
        └── ApplicationTests.java
```

## Logging

The application uses SLF4J with Logback for comprehensive logging:

- **Root Level**: INFO
- **Application Level**: DEBUG
- **Spring Web**: DEBUG
- **Hibernate SQL**: DEBUG

Logs include transaction tracking for all CRUD operations.

## Future Enhancements

- Database persistence with MySQL integration
- Authentication and authorization with Spring Security
- Event categorization and filtering
- Venue search by location
- Ticket reservation system
- Payment gateway integration
- Email notifications
- User management
- Event analytics and reporting

## Version Information

- **Version**: 1.0.0-SNAPSHOT
- **Spring Boot**: 3.5.6
- **Java**: 21
- **Build Tool**: Maven

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Contact

**Ticketing Team**  
Email: soporte@ticketing.com  
Website: https://www.ticketing.com

---

For detailed API documentation and interactive testing, visit the Swagger UI at http://localhost:8080/api/swagger-ui.html after starting the application.
