# Event Management API with JWT Authentication

## Overview

This project represents a production-ready Event Management System built with Spring Boot 3.2.3, implementing Hexagonal Architecture combined with JWT-based stateless authentication and comprehensive Swagger/OpenAPI documentation. The application extends a clean architecture foundation with enterprise-grade security features, providing a secure REST API for managing events and venues.

The system demonstrates the integration of modern security practices with clean architecture principles, showcasing how to implement authentication and authorization without compromising domain purity and architectural boundaries.

## Key Features

### Security Features
- **JWT Authentication**: Stateless token-based authentication using JSON Web Tokens
- **BCrypt Password Hashing**: Secure password storage with BCrypt algorithm
- **Spring Security Integration**: Complete security configuration with method-level security
- **Role-Based Access Control**: User authentication and authorization
- **Stateless Sessions**: No server-side session storage, fully stateless architecture
- **Token Validation**: Automatic JWT validation on every request

### Architectural Features
- **Hexagonal Architecture**: Clean separation of concerns across layers
- **Domain-Driven Design**: Pure domain models without framework dependencies
- **Port and Adapter Pattern**: Well-defined contracts between layers
- **Use Case Driven**: Application layer implements clear use cases
- **Dependency Inversion**: Framework code depends on domain, not vice versa

### API Features
- **Swagger/OpenAPI 3.0**: Interactive API documentation with JWT support
- **RESTful Design**: Standard HTTP methods and status codes
- **Bean Validation**: Input validation with Jakarta Validation
- **Custom Validation**: Date range validation and validation groups
- **Exception Handling**: Global error handling with meaningful responses

### Technical Features
- **JWT Library**: JJWT (Java JWT) for token generation and validation
- **Lombok Integration**: Reduced boilerplate code
- **MySQL Persistence**: Production-grade relational database
- **Spring Data JPA**: Data access with repository pattern
- **DTO Pattern**: Separation of API contracts from domain models

## Technology Stack

### Core Technologies
- **Java 21**: Latest LTS version with modern language features
- **Spring Boot 3.2.3**: Enterprise application framework
- **Maven**: Dependency management and build automation

### Security Stack
- **Spring Security 6.x**: Authentication and authorization framework
- **JJWT 0.11.5**: JWT creation and validation library
  - `jjwt-api`: API for JWT operations
  - `jjwt-impl`: Implementation library
  - `jjwt-jackson`: JSON serialization with Jackson

### Database Layer
- **MySQL 8.0**: Relational database management system
- **Spring Data JPA**: Data persistence abstraction
- **Hibernate**: ORM implementation

### Documentation
- **SpringDoc OpenAPI 2.3.0**: Swagger/OpenAPI 3.0 integration
- **Swagger UI**: Interactive API documentation interface

### Additional Dependencies
- **Jakarta Validation**: Bean Validation 3.0
- **Lombok**: Code generation for DTOs and entities
- **BCrypt**: Password hashing algorithm

## Architecture

### Hexagonal Architecture with Security Layer

```
┌─────────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER                         │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              REST Controllers (Adapters)                  │  │
│  │  AuthController │ EventController │ VenueController      │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                  Security Layer                          │  │
│  │  JwtAuthenticationFilter                                 │  │
│  │  SecurityConfig                                          │  │
│  │  JwtService                                              │  │
│  │  CustomUserDetailsService                                │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │           Persistence Adapters                           │  │
│  │  EventPersistenceAdapter                                 │  │
│  │  VenuePersistenceAdapter                                 │  │
│  │  UserPersistenceAdapter                                  │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │        JPA Repositories & Entities                       │  │
│  │  JpaUserRepository │ JpaEventRepository │ JpaVenueRepo   │  │
│  │  UserEntity │ EventEntity │ VenueEntity                  │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                    APPLICATION LAYER                            │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                   Use Cases                              │  │
│  │  AuthUseCaseImpl                                         │  │
│  │  EventUseCaseImpl                                        │  │
│  │  VenueUseCaseImpl                                        │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                      DOMAIN LAYER                               │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │         Pure Domain Models (No dependencies)             │  │
│  │  User │ Event │ Venue │ EventStatus                      │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                     Ports                                │  │
│  │  IN:  AuthUseCase │ EventUseCase │ VenueUseCase         │  │
│  │  OUT: UserRepositoryPort │ EventRepositoryPort          │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

### Project Structure

```
src/
├── main/
│   ├── java/com/example/HU4/
│   │   ├── domain/                        # DOMAIN LAYER
│   │   │   ├── model/                     # Pure domain models
│   │   │   │   ├── User.java
│   │   │   │   ├── Event.java
│   │   │   │   ├── Venue.java
│   │   │   │   └── EventStatus.java
│   │   │   └── ports/                     # Port interfaces
│   │   │       ├── in/                    # Inbound ports (use cases)
│   │   │       │   ├── AuthUseCase.java
│   │   │       │   ├── EventUseCase.java
│   │   │       │   └── VenueUseCase.java
│   │   │       └── out/                   # Outbound ports
│   │   │           ├── UserRepositoryPort.java
│   │   │           ├── EventRepositoryPort.java
│   │   │           └── VenueRepositoryPort.java
│   │   │
│   │   ├── application/                   # APPLICATION LAYER
│   │   │   └── usecases/                  # Use case implementations
│   │   │       ├── AuthUseCaseImpl.java
│   │   │       ├── EventUseCaseImpl.java
│   │   │       └── VenueUseCaseImpl.java
│   │   │
│   │   ├── infrastructure/                # INFRASTRUCTURE LAYER
│   │   │   ├── adapters/
│   │   │   │   ├── in/
│   │   │   │   │   └── rest/              # REST controllers
│   │   │   │   │       ├── AuthController.java
│   │   │   │   │       ├── EventController.java
│   │   │   │   │       └── VenueController.java
│   │   │   │   └── out/
│   │   │   │       └── persistence/       # Persistence adapters
│   │   │   │           ├── UserPersistenceAdapter.java
│   │   │   │           ├── EventPersistenceAdapter.java
│   │   │   │           └── VenuePersistenceAdapter.java
│   │   │   ├── config/                    # Configuration
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── SwaggerConfig.java
│   │   │   │   ├── WebMvcConfig.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── dto/                       # Data Transfer Objects
│   │   │   │   ├── AuthRequest.java
│   │   │   │   ├── EventRequest.java
│   │   │   │   ├── EventResponse.java
│   │   │   │   ├── VenueRequest.java
│   │   │   │   └── VenueResponse.java
│   │   │   ├── entities/                  # JPA entities
│   │   │   │   ├── UserEntity.java
│   │   │   │   ├── EventEntity.java
│   │   │   │   └── VenueEntity.java
│   │   │   ├── mappers/                   # Entity/Domain mappers
│   │   │   │   ├── EventMapper.java
│   │   │   │   └── VenueMapper.java
│   │   │   ├── repositories/              # Spring Data JPA
│   │   │   │   ├── JpaUserRepository.java
│   │   │   │   ├── JpaEventRepository.java
│   │   │   │   └── JpaVenueRepository.java
│   │   │   ├── security/                  # Security components
│   │   │   │   ├── JwtService.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   └── validation/                # Custom validators
│   │   │       ├── DateRange.java
│   │   │       ├── DateRangeValidator.java
│   │   │       ├── CreateGroup.java
│   │   │       └── UpdateGroup.java
│   │   │
│   │   └── HU5Application.java            # Main application
│   │
│   └── resources/
│       └── application.properties         # Configuration
│
└── test/
    └── java/com/example/HU4/
```

## JWT Security Flow

### Authentication Flow

```
┌─────────┐                ┌──────────────┐              ┌────────────┐
│ Client  │                │ Auth         │              │ Auth       │
│         │                │ Controller   │              │ Use Case   │
└─────────┘                └──────────────┘              └────────────┘
     │                            │                             │
     │ POST /api/auth/login       │                             │
     │ (username, password)       │                             │
     ├───────────────────────────>│                             │
     │                            │ authenticate(user, pass)    │
     │                            ├────────────────────────────>│
     │                            │                             │
     │                            │                             │ Validate
     │                            │                             │ credentials
     │                            │                             │
     │                            │      User authenticated     │
     │                            │<────────────────────────────┤
     │                            │                             │
     │                            │ Generate JWT Token          │
     │                            ├──────────────────────>      │
     │                            │                        ┌────┴────┐
     │                            │      JWT Token         │   JWT   │
     │      { "token": "..." }    │<───────────────────────│ Service │
     │<───────────────────────────┤                        └─────────┘
     │                            │
```

### Authorization Flow

```
┌─────────┐               ┌─────────────────┐          ┌────────────┐
│ Client  │               │ JWT Filter      │          │ Spring     │
│         │               │                 │          │ Security   │
└─────────┘               └─────────────────┘          └────────────┘
     │                           │                           │
     │ GET /events              │                           │
     │ Authorization: Bearer ... │                           │
     ├──────────────────────────>│                           │
     │                           │ Extract & Validate JWT    │
     │                           │                           │
     │                           │ Set Authentication        │
     │                           ├──────────────────────────>│
     │                           │                           │
     │                           │                           │ Check
     │                           │                           │ permissions
     │                           │                           │
     │                           │  Authorized               │
     │      Events data          │<──────────────────────────┤
     │<──────────────────────────┤                           │
```

## API Endpoints

### Authentication Endpoints (Public)

#### Register New User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "password": "SecurePassword123!"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNzAxNTQwMDAwLCJleHAiOjE3MDE2MjY0MDB9.xxx"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "SecurePassword123!"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNzAxNTQwMDAwLCJleHAiOjE3MDE2MjY0MDB9.xxx"
}
```

### Event Endpoints (Protected)

All event endpoints require JWT authentication via the `Authorization` header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Create Event
```http
POST /events
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Tech Conference 2025",
  "description": "Annual technology conference",
  "startDate": "2025-06-15T09:00:00",
  "endDate": "2025-06-15T18:00:00",
  "status": "ACTIVE",
  "venueId": 1
}

Response: 201 Created
{
  "id": 1,
  "name": "Tech Conference 2025",
  "description": "Annual technology conference",
  "startDate": "2025-06-15T09:00:00",
  "endDate": "2025-06-15T18:00:00",
  "status": "ACTIVE",
  "venue": {
    "id": 1,
    "name": "Convention Center",
    "location": "Downtown"
  }
}
```

#### Get All Events
```http
GET /events
Authorization: Bearer {token}

Response: 200 OK
[
  {
    "id": 1,
    "name": "Tech Conference 2025",
    ...
  }
]
```

#### Get Event by ID
```http
GET /events/{id}
Authorization: Bearer {token}

Response: 200 OK
```

#### Update Event
```http
PUT /events/{id}
Authorization: Bearer {token}
Content-Type: application/json

Response: 200 OK
```

#### Delete Event
```http
DELETE /events/{id}
Authorization: Bearer {token}

Response: 204 No Content
```

### Venue Endpoints (Protected)

Same authentication pattern as Events:

- `POST /venues` - Create venue
- `GET /venues` - Get all venues
- `GET /venues/{id}` - Get venue by ID
- `PUT /venues/{id}` - Update venue
- `DELETE /venues/{id}` - Delete venue

## Configuration

### Application Properties

```properties
# Application Configuration
spring.application.name=HU4-Event-Management

# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/event_management?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=Qwe.123*
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.open-in-view=false

# JWT Configuration
jwt.secret=your-secret-key-change-this-in-production-minimum-256-bits-required
jwt.expiration=86400000
# Token expiration: 86400000ms = 24 hours

# Logging Configuration
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
logging.level.com.example.HU4=DEBUG
```

### Security Configuration

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

## JWT Implementation Details

### JWT Service

The `JwtService` class handles all JWT operations:

#### Token Generation
```java
public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails.getUsername());
}

private String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
}
```

#### Token Validation
```java
public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
}
```

#### Token Extraction
```java
public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
}

public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
}
```

### JWT Authentication Filter

The `JwtAuthenticationFilter` intercepts every request and validates the JWT:

```java
@Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain) {
    final String authHeader = request.getHeader("Authorization");
    
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (jwtService.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    }
    
    filterChain.doFilter(request, response);
}
```

## Swagger/OpenAPI Integration

### Accessing API Documentation

Once the application is running, access the interactive Swagger UI at:

**http://localhost:8080/swagger-ui.html**

### Using JWT in Swagger

1. **Register or Login** using the auth endpoints to get a JWT token
2. Click the **"Authorize"** button in Swagger UI (top right)
3. Enter the token in the format: `Bearer {your_token_here}`
4. Click **"Authorize"**
5. All subsequent requests will include the JWT token automatically

### Swagger Configuration

```java
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HU5 Event Management API")
                        .description("API with JWT authentication")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
```

## Custom Validation

### Date Range Validation

Custom annotation to validate that end date is after start date:

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface DateRange {
    String message() default "End date must be after start date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String start();
    String end();
}
```

**Usage:**
```java
@DateRange(start = "startDate", end = "endDate")
public class EventRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
```

### Validation Groups

Separate validation rules for create and update operations:

```java
public class EventRequest {
    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private Long id;
    
    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class})
    private String name;
}
```

## Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **MySQL 8.0+**
- **Postman** or similar HTTP client (optional, for testing)

### Database Setup

1. Start MySQL server

2. Create database (or let Spring create it automatically):
```sql
CREATE DATABASE event_management;
```

3. Configure credentials in `application.properties` if needed

### Installation and Running

1. Clone and checkout HU-5 branch:
```bash
git clone <repository-url>
cd SPRINGBOOT-MODULE-6
git checkout HU-5
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

### Testing the Application

#### Step 1: Register a User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcwMTU0MDAwMCwiZXhwIjoxNzAxNjI2NDAwfQ.xxx"
}
```

#### Step 2: Use Token for Authenticated Requests

```bash
# Store the token in a variable (Linux/Mac)
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# Create a venue
curl -X POST http://localhost:8080/venues \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Convention Center",
    "location": "Downtown",
    "capacity": 5000,
    "description": "Modern convention center"
  }'

# Create an event
curl -X POST http://localhost:8080/events \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tech Summit 2025",
    "description": "Technology conference",
    "startDate": "2025-06-15T09:00:00",
    "endDate": "2025-06-15T18:00:00",
    "status": "ACTIVE",
    "venueId": 1
  }'

# Get all events
curl -X GET http://localhost:8080/events \
  -H "Authorization: Bearer $TOKEN"
```

#### Step 3: Try Accessing Without Token (Should Fail)

```bash
curl -X GET http://localhost:8080/events

# Response: 403 Forbidden (no token provided)
```

## Security Best Practices

### 1. JWT Secret Key

**IMPORTANT**: Change the default JWT secret in production!

```properties
# Use a strong, random secret key (minimum 256 bits for HS256)
jwt.secret=your-very-long-and-random-secret-key-generated-with-cryptographic-tools

# Generate a secure key (Linux/Mac):
openssl rand -base64 64
```

### 2. Password Requirements

Implement password complexity requirements in production:

```java
@NotBlank
@Size(min = 8, message = "Password must be at least 8 characters")
@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", 
         message = "Password must contain uppercase, lowercase, and number")
private String password;
```

### 3. Token Expiration

Consider shorter expiration times for production:

```properties
# 1 hour = 3600000ms
jwt.expiration=3600000

# Or implement refresh tokens for better UX
```

### 4. HTTPS Only

In production, enforce HTTPS:

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http
        .requiresChannel(channel -> channel
            .anyRequest().requiresSecure());
    // ... rest of configuration
}
```

### 5. Rate Limiting

Implement rate limiting to prevent brute force attacks:

```java
// Consider using Spring Cloud Gateway or Bucket4j
```

## Common Issues and Solutions

### Issue 1: Invalid JWT Token

**Error:** "JWT token is invalid or expired"

**Causes:**
- Token expired (24 hours default)
- Invalid secret key
- Token was modified

**Solution:**
- Login again to get a new token
- Verify JWT secret matches between generation and validation

### Issue 2: 403 Forbidden

**Error:** "Access Denied"

**Causes:**
- No token provided
- Token missing "Bearer " prefix
- Token in wrong header

**Solution:**
```bash
# Correct format
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

# Wrong formats
Authorization: eyJhbGciOiJIUzI1NiJ9...  # Missing "Bearer "
auth: Bearer ...                        # Wrong header name
```

### Issue 3: CORS Errors

**Error:** "CORS policy: No 'Access-Control-Allow-Origin' header"

**Solution:**
Check `WebMvcConfig` or add CORS configuration:

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
```

### Issue 4: BCrypt Hashing Performance

**Symptom:** Slow login/registration

**Explanation:** BCrypt is intentionally slow (10-12 rounds recommended)

**Solution:** This is expected behavior. Don't reduce strength rounds in production.

## Domain Models

### User (Pure Domain)

```java
@Builder
public class User {
    private Long id;
    private String username;
    private String password;  // Plain text in domain, hashed in infrastructure
    private String role;
}
```

### Event (Pure Domain)

```java
public class Event {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventStatus status;
    private Long venueId;
}
```

### Venue (Pure Domain)

```java
public class Venue {
    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private String description;
}
```

## Benefits of This Architecture

### 1. Security Without Compromising Clean Architecture

- JWT logic is in infrastructure layer
- Domain remains pure and framework-agnostic
- Security is added as a cross-cutting concern

### 2. Stateless Authentication

- No server-side sessions
- Horizontal scaling without session replication
- Works with multiple servers/load balancers

### 3. API Documentation with Security

- Swagger UI fully integrated with JWT
- Easy testing with built-in authorization
- Self-documenting API

### 4. Separation of Concerns

- Authentication logic separate from business logic
- Easy to change authentication strategy
- Domain models remain unchanged

### 5. Production Ready

- BCrypt password hashing
- JWT token validation
- Method-level security
- Comprehensive error handling

## Testing Strategy

### Unit Testing

**Test domain models in isolation:**
```java
@Test
void testUserCreation() {
    User user = User.builder()
        .username("testuser")
        .password("password")
        .build();
    
    assertEquals("testuser", user.getUsername());
}
```

### Integration Testing

**Test with Security Context:**
```java
@SpringBootTest
@AutoConfigureMockMvc
class EventControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithMockUser(username = "testuser")
    void testGetEventsAuthenticated() throws Exception {
        mockMvc.perform(get("/events"))
            .andExpect(status().isOk());
    }
}
```

### Security Testing

**Test authentication requirement:**
```java
@Test
void testGetEventsWithoutAuth() throws Exception {
    mockMvc.perform(get("/events"))
        .andExpect(status().isForbidden());
}
```

## Future Enhancements

### Security Features
- **Refresh Tokens**: Implement token refresh mechanism
- **Role-Based Access**: Add ADMIN, USER, MANAGER roles
- **OAuth 2.0**: Integration with OAuth providers (Google, GitHub)
- **Two-Factor Authentication**: Additional security layer
- **Account Lockout**: Prevent brute force attacks
- **Password Reset**: Email-based password recovery

### Advanced Features
- **Rate Limiting**: Protect against DDoS and brute force
- **Audit Logging**: Track all user actions
- **API Versioning**: Support multiple API versions
- **Webhook Support**: Event notifications
- **File Upload**: Event images and attachments
- **Email Notifications**: Event reminders

### Monitoring
- **Spring Boot Actuator**: Application metrics
- **Micrometer**: Metrics collection
- **Prometheus/Grafana**: Monitoring dashboard
- **Log Aggregation**: Centralized logging (ELK Stack)

## Version Information

- **Application Version**: 1.0.0
- **Spring Boot**: 3.2.3
- **Spring Security**: 6.x
- **Java**: 21
- **JJWT**: 0.11.5
- **SpringDoc OpenAPI**: 2.3.0

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Resources

### JWT
- [JWT.io](https://jwt.io/) - JWT debugger and documentation
- [JJWT Documentation](https://github.com/jwtk/jjwt)
- [RFC 7519](https://tools.ietf.org/html/rfc7519) - JWT specification

### Spring Security
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture)

### Best Practices
- [OWASP Security Guidelines](https://owasp.org/www-project-web-security-testing-guide/)
- [JWT Best Practices](https://datatracker.ietf.org/doc/html/rfc8725)

---

This project demonstrates production-ready Spring Boot development with JWT authentication, hexagonal architecture, and comprehensive security practices.
