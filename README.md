# Hexagonal Architecture - Clean Architecture Implementation

## Overview

This project is a reference implementation of Hexagonal Architecture (also known as Ports and Adapters) using Spring Boot 3.5.0 and Java 21. The application demonstrates clean architecture principles through a user management system, showcasing complete separation of business logic from infrastructure concerns.

Hexagonal Architecture, introduced by Alistair Cockburn, organizes code into three main layers: Domain (core business logic), Application (use cases), and Infrastructure (external adapters), with clear boundaries defined through ports and adapters.

## Architecture Fundamentals

### What is Hexagonal Architecture?

Hexagonal Architecture is an architectural pattern that aims to create loosely coupled application components that can be easily connected to their software environment through ports and adapters. This makes the application:

- **Independent of frameworks**: The business logic doesn't depend on Spring, JPA, or any other framework
- **Testable**: Business logic can be tested without databases, web servers, or external dependencies
- **Independent of UI**: The UI can change without affecting business rules
- **Independent of databases**: Database can be swapped without affecting business logic
- **Independent of any external agency**: Business rules don't know anything about the outside world

### Core Principles

1. **Dependency Inversion**: High-level modules (domain) don't depend on low-level modules (infrastructure)
2. **Separation of Concerns**: Each layer has a single, well-defined responsibility
3. **Domain-Centric Design**: Business logic is the heart of the application
4. **Ports and Adapters**: Communication happens through well-defined interfaces

## Project Structure

```
src/main/java/com/example/
├── dominio/                           # DOMAIN LAYER (Core Business Logic)
│   ├── model/                         # Pure domain models
│   │   └── Usuario.java               # Domain entity (no JPA annotations)
│   └── ports/                         # Port interfaces (contracts)
│       ├── in/                        # Inbound ports (Use Cases)
│       │   ├── CrearUsuarioUseCase.java
│       │   ├── ObtenerUsuarioUseCase.java
│       │   └── EliminarUsuarioUseCase.java
│       └── out/                       # Outbound ports (Repository contracts)
│           └── UsuarioRepositoryPort.java
│
├── aplicacion/                        # APPLICATION LAYER (Use Case Implementation)
│   └── usecase/
│       └── UsuarioService.java        # Use case orchestration
│
├── infraestructura/                   # INFRASTRUCTURE LAYER (Adapters)
│   ├── adapters/
│   │   ├── in/                        # Inbound adapters (drivers)
│   │   │   └── web/                   # REST API adapter
│   │   │       ├── UsuarioRestAdapter.java
│   │   │       ├── dto/               # Request/Response DTOs
│   │   │       │   ├── UsuarioRequest.java
│   │   │       │   └── UsuarioResponse.java
│   │   │       └── mapper/            # DTO mappers (MapStruct)
│   │   │           └── UsuarioDtoMapper.java
│   │   └── out/                       # Outbound adapters (driven)
│   │       └── jpa/                   # Database adapter
│   │           ├── UsuarioJpaAdapter.java
│   │           ├── entity/            # JPA entities
│   │           │   └── UsuarioEntity.java
│   │           ├── mapper/            # Entity mappers (MapStruct)
│   │           │   └── UsuarioMapper.java
│   │           └── repository/        # Spring Data JPA repository
│   │               └── UsuarioJpaRepository.java
│   └── config/                        # Spring configuration
│       └── ApplicationConfig.java     # Bean wiring
│
└── Application.java                   # Spring Boot entry point
```

## Layer Descriptions

### 1. Domain Layer (Dominio)

The innermost layer containing pure business logic with no external dependencies.

#### Domain Models
**Usuario.java** - Pure Java class representing the business entity:
- No JPA annotations
- No Spring dependencies
- Contains only business data and logic
- Framework-agnostic

#### Ports (Interfaces)

**Inbound Ports** (`ports/in/`): Define what the application can do (Use Cases)
- `CrearUsuarioUseCase`: Contract for creating users
- `ObtenerUsuarioUseCase`: Contract for retrieving users
- `EliminarUsuarioUseCase`: Contract for deleting users

**Outbound Ports** (`ports/out/`): Define what the application needs (Repository contracts)
- `UsuarioRepositoryPort`: Repository contract for persistence operations

### 2. Application Layer (Aplicacion)

Contains use case implementations that orchestrate domain logic.

**UsuarioService.java**:
- Implements all inbound port interfaces (use cases)
- Depends only on domain models and port interfaces
- Contains business validation logic
- Technology-agnostic (no Spring annotations on the class itself)
- Injected as a Spring bean via configuration

### 3. Infrastructure Layer (Infraestructura)

Contains all framework-specific code and external integrations.

#### Inbound Adapters (Input/Driving Adapters)

**REST API Adapter** (`adapters/in/web/`):
- **UsuarioRestAdapter**: Spring REST controller
  - Exposes HTTP endpoints
  - Converts HTTP requests to use case calls
  - Depends on inbound port interfaces (not implementations)
  
- **DTOs**:
  - `UsuarioRequest`: Input DTO for creating users
  - `UsuarioResponse`: Output DTO for API responses
  
- **UsuarioDtoMapper**: MapStruct mapper
  - Converts between DTOs and domain models
  - Type-safe compile-time generation

#### Outbound Adapters (Output/Driven Adapters)

**JPA Database Adapter** (`adapters/out/jpa/`):
- **UsuarioJpaAdapter**: Implements UsuarioRepositoryPort
  - Adapts domain repository contract to Spring Data JPA
  - Converts between domain models and JPA entities
  
- **UsuarioEntity**: JPA entity
  - Contains JPA annotations
  - Represents database structure
  - Isolated from domain layer
  
- **UsuarioMapper**: MapStruct mapper
  - Converts between domain models and JPA entities
  
- **UsuarioJpaRepository**: Spring Data JPA interface
  - Standard Spring Data repository
  - Never exposed to domain layer

#### Configuration

**ApplicationConfig.java**:
- Wires everything together
- Creates beans for use case implementations
- Dependency injection configuration

## Dependency Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER                      │
│  ┌────────────────────┐                ┌─────────────────┐  │
│  │  REST Adapter      │                │   JPA Adapter   │  │
│  │ (UsuarioRestAdapter)│               │(UsuarioJpaAdapter)│ │
│  └─────────┬──────────┘                └────────┬────────┘  │
│            │                                     │           │
│            │            implements               │           │
│            ▼                                     ▼           │
├───────────────────────────────────────────────────────────────┤
│                    APPLICATION LAYER                         │
│            ┌─────────────────────────────────┐               │
│            │      UsuarioService             │               │
│            │  (implements use case ports)    │               │
│            └────────────┬────────────────────┘               │
│                         │                                    │
│                         │ uses                               │
│                         ▼                                    │
├───────────────────────────────────────────────────────────────┤
│                      DOMAIN LAYER                            │
│  ┌──────────────┐   ┌─────────────────────────────────┐     │
│  │   Usuario    │   │         Ports                   │     │
│  │ (pure domain │   │  - CrearUsuarioUseCase          │     │
│  │   model)     │   │  - ObtenerUsuarioUseCase        │     │
│  │              │   │  - EliminarUsuarioUseCase       │     │
│  │              │   │  - UsuarioRepositoryPort        │     │
│  └──────────────┘   └─────────────────────────────────┘     │
└───────────────────────────────────────────────────────────────┘

        ▲                                     ▲
        │                                     │
    Dependencies point INWARD (Dependency Inversion)
```

## Key Features

### Architectural Features

- **Pure Domain Layer**: Business logic with zero framework dependencies
- **Ports and Adapters**: Clear contracts between layers
- **Dependency Inversion**: All dependencies point inward to the domain
- **Interchangeable Adapters**: Easy to swap REST with GraphQL, JPA with MongoDB, etc.
- **Testability**: Each layer can be tested in isolation

### Technical Features

- **MapStruct Integration**: Compile-time, type-safe object mapping
- **Spring Boot 3.5.0**: Latest Spring Boot with Java 21 support
- **MySQL Persistence**: Production-ready database integration
- **RESTful API**: Complete CRUD operations via HTTP
- **Bean Validation**: Input validation with Jakarta Validation
- **CORS Support**: Cross-origin requests enabled for frontend integration

## Technology Stack

### Core Technologies
- **Java 21**: Latest LTS version with modern language features
- **Spring Boot 3.5.0**: Application framework (used only in infrastructure layer)
- **Maven**: Build automation and dependency management

### Key Dependencies

- **Spring Web**: REST API implementation (infrastructure only)
- **Spring Data JPA**: Database integration (infrastructure only)
- **MySQL Connector**: Database driver
- **Jakarta Validation**: Bean validation
- **MapStruct 1.5.5**: Compile-time object mapping

### Architecture Pattern
- **Hexagonal Architecture** (Ports and Adapters)
- **Clean Architecture** principles
- **Domain-Driven Design** (DDD) influences

## API Endpoints

### User Management

#### Create User
```
POST /api/usuarios
Content-Type: application/json

{
  "username": "johndoe",
  "password": "securepass123",
  "role": "ADMIN"
}

Response: 201 Created
{
  "id": 1,
  "username": "johndoe",
  "password": "securepass123",
  "role": "ADMIN"
}
```

#### Get User by ID
```
GET /api/usuarios/{id}

Response: 200 OK
{
  "id": 1,
  "username": "johndoe",
  "password": "securepass123",
  "role": "ADMIN"
}

Response: 404 Not Found (if user doesn't exist)
```

#### Get All Users
```
GET /api/usuarios

Response: 200 OK
[
  {
    "id": 1,
    "username": "johndoe",
    "password": "securepass123",
    "role": "ADMIN"
  },
  {
    "id": 2,
    "username": "janedoe",
    "password": "pass456",
    "role": "USER"
  }
]
```

#### Delete User
```
DELETE /api/usuarios/{id}

Response: 204 No Content
```

## Configuration

### Database Configuration

MySQL configuration in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hexagonal_db?createDatabaseIfNotExist=true
    username: root
    password: Qwe.123*
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQLDialect
```

### Server Configuration

```yaml
server:
  port: 8080

logging:
  level:
    com.example: DEBUG
```

## Getting Started

### Prerequisites

- **Java Development Kit (JDK) 21** or higher
- **Apache Maven 3.6+** for building the project
- **MySQL 8.0+** database server
- **Git** for version control

### Database Setup

1. Start MySQL server

2. Create database (or let Spring create it automatically):
```sql
CREATE DATABASE hexagonal_db;
```

3. Update credentials in `application.yml` if needed

### Installation and Running

1. Clone the repository:
```bash
git clone <repository-url>
cd SPRINGBOOT-MODULE-6
git checkout HU-3
```

2. Build the project:
```bash
./mvnw clean install
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

4. The API will be available at: `http://localhost:8080/api`

### Testing the API

Using curl:

```bash
# Create a user
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"pass123","role":"USER"}'

# Get all users
curl http://localhost:8080/api/usuarios

# Get user by ID
curl http://localhost:8080/api/usuarios/1

# Delete user
curl -X DELETE http://localhost:8080/api/usuarios/1
```

## MapStruct Configuration

MapStruct is configured for compile-time code generation. The Maven compiler plugin is configured with annotation processing:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>21</source>
        <target>21</target>
        <annotationProcessorPaths>
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>1.5.5.Final</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

Generated mapper implementations can be found in `target/generated-sources/annotations/`.

## Benefits of This Architecture

### 1. Testability

Each layer can be tested independently:

- **Domain Layer**: Test pure business logic without any infrastructure
- **Application Layer**: Test use cases with mock repositories
- **Infrastructure Layer**: Test adapters with in-memory databases or test containers

### 2. Flexibility

Easy to swap implementations:

- Change from REST to GraphQL: Create new inbound adapter
- Change from MySQL to MongoDB: Create new outbound adapter
- Add new use cases: Extend domain ports without modifying existing code

### 3. Maintainability

- Clear separation of concerns
- Each layer has a single responsibility
- Changes in one layer don't cascade to others
- Business logic is protected from technology changes

### 4. Domain Focus

- Business rules are explicit and centralized
- Domain models are pure and framework-agnostic
- Business logic can be understood without framework knowledge

### 5. Technology Independence

- Domain layer has no Spring, JPA, or HTTP dependencies
- Business logic can be reused in different contexts
- Framework upgrades don't affect business logic

## Design Patterns Used

### Hexagonal Architecture (Ports and Adapters)
- **Ports**: Interfaces defining contracts (in `ports/in` and `ports/out`)
- **Adapters**: Implementations connecting to external systems

### Dependency Inversion Principle
- High-level modules (domain) don't depend on low-level modules (infrastructure)
- Both depend on abstractions (ports)

### Adapter Pattern
- REST adapter adapts HTTP to use cases
- JPA adapter adapts domain repository to Spring Data JPA

### Repository Pattern
- `UsuarioRepositoryPort` defines repository contract
- `UsuarioJpaAdapter` implements persistence logic

### Service Layer Pattern
- `UsuarioService` orchestrates use cases
- Separates business logic from presentation and data access

### DTO Pattern
- `UsuarioRequest` and `UsuarioResponse` for API communication
- Protects domain models from external exposure

### Mapper Pattern
- MapStruct mappers for type-safe conversions
- Separates domain models from DTOs and entities

## Common Pitfalls to Avoid

### 1. Dependency Leakage
**Wrong**: Domain layer depending on JPA annotations or Spring
```java
// ❌ BAD - Domain model with JPA
@Entity
public class Usuario {
    @Id
    private Long id;
}
```

**Correct**: Pure domain model
```java
// ✅ GOOD - Pure domain
public class Usuario {
    private Long id;
}
```

### 2. Use Case Pollution
**Wrong**: HTTP concerns in use case layer
```java
// ❌ BAD
public ResponseEntity<Usuario> crearUsuario(HttpServletRequest request) { }
```

**Correct**: Technology-agnostic use cases
```java
// ✅ GOOD
public Usuario crearUsuario(Usuario usuario) { }
```

### 3. Adapter Bypassing
**Wrong**: Controllers calling repositories directly
```java
// ❌ BAD
@RestController
public class UsuarioController {
    @Autowired
    private UsuarioRepository repository; // Direct dependency
}
```

**Correct**: Controllers depending on use cases
```java
// ✅ GOOD
@RestController
public class UsuarioRestAdapter {
    private final CrearUsuarioUseCase crearUsuarioUseCase;
}
```

## Testing Strategy

### Unit Testing

**Domain Layer**:
```java
@Test
void testUsuarioCreation() {
    Usuario usuario = new Usuario(null, "test", "pass", "USER");
    assertNotNull(usuario);
    assertEquals("test", usuario.getUsername());
}
```

**Application Layer**:
```java
@Test
void testCrearUsuario() {
    UsuarioRepositoryPort mockRepo = mock(UsuarioRepositoryPort.class);
    UsuarioService service = new UsuarioService(mockRepo);
    
    Usuario usuario = new Usuario(null, "test", "pass", "USER");
    when(mockRepo.save(any())).thenReturn(usuario);
    
    Usuario result = service.crearUsuario(usuario);
    verify(mockRepo).save(usuario);
}
```

### Integration Testing

Test adapters with real infrastructure:
```java
@SpringBootTest
@Transactional
class UsuarioJpaAdapterIntegrationTest {
    @Autowired
    private UsuarioRepositoryPort repository;
    
    @Test
    void testSaveAndFindUser() {
        Usuario saved = repository.save(new Usuario(...));
        Optional<Usuario> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
    }
}
```

## Future Enhancements

### Additional Adapters
- **GraphQL Adapter**: Alternative inbound adapter for GraphQL API
- **Message Queue Adapter**: Event-driven communication (Kafka, RabbitMQ)
- **MongoDB Adapter**: Alternative persistence implementation
- **Redis Adapter**: Caching layer

### Domain Enhancements
- **Password Encryption**: Hash passwords in domain layer
- **Role-Based Access Control**: Advanced authorization
- **User Profiles**: Extended user information
- **Audit Trail**: Track user changes

### Infrastructure Improvements
- **Spring Security**: Authentication and authorization
- **JWT Tokens**: Stateless authentication
- **Swagger/OpenAPI**: API documentation
- **Docker**: Containerization
- **Kubernetes**: Orchestration

### Testing
- **Archunit**: Enforce architectural rules
- **Testcontainers**: Integration testing with real databases
- **Cucumber**: Behavior-driven development

## Comparison: Hexagonal vs Traditional Layered

| Aspect | Traditional Layered | Hexagonal Architecture |
|--------|-------------------|----------------------|
| **Domain Dependencies** | Often depends on JPA, Spring | Zero framework dependencies |
| **Testing** | Requires infrastructure setup | Can test business logic in pure Java |
| **Flexibility** | Tightly coupled to frameworks | Easy to swap adapters |
| **Reusability** | Limited to specific technology | Business logic reusable anywhere |
| **Focus** | Technology-driven | Domain-driven |
| **Complexity** | Lower initial complexity | Higher initial setup cost |
| **Maintainability** | Medium | High |

## Project Philosophy

This project follows these key principles:

1. **Business Logic First**: The domain is the core, technology is secondary
2. **Explicit Boundaries**: Clear separation through ports and adapters
3. **Dependency Discipline**: Strict adherence to dependency rules
4. **Technology Agnostic**: Business logic works without frameworks
5. **Testability**: Every component is easily testable in isolation

## Version Information

- **Application Version**: 1.0.0
- **Spring Boot**: 3.5.0
- **Java**: 21
- **MapStruct**: 1.5.5.Final
- **Build Tool**: Maven

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Learning Resources

### Hexagonal Architecture
- [Alistair Cockburn - Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Netflix - Ready for changes with Hexagonal Architecture](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749)

### Clean Architecture
- Robert C. Martin - "Clean Architecture: A Craftsman's Guide to Software Structure and Design"
- [The Clean Architecture by Uncle Bob](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

### Domain-Driven Design
- Eric Evans - "Domain-Driven Design: Tackling Complexity in the Heart of Software"
- Vaughn Vernon - "Implementing Domain-Driven Design"

---

This implementation serves as a reference for building maintainable, testable, and flexible applications using Hexagonal Architecture principles with Spring Boot and Java 21.
