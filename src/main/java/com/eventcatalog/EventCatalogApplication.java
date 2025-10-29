package com.eventcatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Clase principal de la aplicación Event Catalog Backend.
 * 
 * @SpringBootApplication: Anotación compuesta que incluye:
 * - @Configuration: Indica que esta clase puede definir beans
 * - @EnableAutoConfiguration: Activa la configuración automática de Spring Boot
 * - @ComponentScan: Escanea y registra componentes en el paquete y subpaquetes
 * 
 * @EnableJpaAuditing: Habilita el auditing de JPA para campos como:
 * - @CreatedDate
 * - @LastModifiedDate
 * - @CreationTimestamp
 * - @UpdateTimestamp
 * 
 * Esto permite que Spring Boot maneje automáticamente las fechas de creación
 * y actualización en las entidades.
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class EventCatalogApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     * 
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(EventCatalogApplication.class, args);
        
        // Mensaje de bienvenida en la consola
        System.out.println("\n" +
            "===============================================\n" +
            "   EVENT CATALOG API - BACKEND INICIADO      \n" +
            "===============================================\n" +
            "   URL: http://localhost:8080/api             \n" +
            "   Perfil activo: dev                         \n" +
            "   Base de datos: MySQL                       \n" +
            "===============================================\n" +
            "\n" +
            "📋 Endpoints disponibles:\n" +
            "\n" +
            "VENUES:\n" +
            "  POST   /api/venues              - Crear lugar\n" +
            "  GET    /api/venues              - Listar lugares\n" +
            "  GET    /api/venues/{id}         - Obtener lugar por ID\n" +
            "  GET    /api/venues/city/{city}  - Buscar por ciudad\n" +
            "  PUT    /api/venues/{id}         - Actualizar lugar\n" +
            "  DELETE /api/venues/{id}         - Eliminar lugar\n" +
            "\n" +
            "EVENTS:\n" +
            "  POST   /api/events                    - Crear evento\n" +
            "  GET    /api/events                    - Listar eventos\n" +
            "  GET    /api/events/{id}               - Obtener evento por ID\n" +
            "  GET    /api/events/city/{city}        - Buscar por ciudad\n" +
            "  GET    /api/events/category/{category}- Buscar por categoría\n" +
            "  GET    /api/events/upcoming           - Eventos futuros\n" +
            "  PUT    /api/events/{id}               - Actualizar evento\n" +
            "  DELETE /api/events/{id}               - Eliminar evento\n" +
            "\n" +
            "===============================================\n"
        );
    }
}

/**
 * ARQUITECTURA DE LA APLICACIÓN:
 * 
 * ┌─────────────────┐
 * │   FRONTEND      │  Angular 20 (http://localhost:4200)
 * │   (Angular)     │
 * └────────┬────────┘
 *          │ HTTP/REST
 *          ▼
 * ┌─────────────────┐
 * │  CONTROLLER     │  @RestController - Maneja peticiones HTTP
 * │  (REST API)     │  - VenueController
 * │                 │  - EventController
 * └────────┬────────┘
 *          │
 *          ▼
 * ┌─────────────────┐
 * │    SERVICE      │  @Service - Lógica de negocio
 * │  (Business)     │  - IVenueService / VenueServiceImpl
 * │                 │  - IEventService / EventServiceImpl
 * └────────┬────────┘
 *          │
 *          ▼
 * ┌─────────────────┐
 * │  REPOSITORY     │  @Repository - Acceso a datos
 * │  (Data Access)  │  - VenueRepository (JpaRepository)
 * │                 │  - EventRepository (JpaRepository)
 * └────────┬────────┘
 *          │
 *          ▼
 * ┌─────────────────┐
 * │    DATABASE     │  MySQL (localhost:3306)
 * │     (MySQL)     │  - Tablas: venues, events
 * └─────────────────┘
 * 
 * 
 * PRINCIPIOS SOLID APLICADOS:
 * 
 * S - Single Responsibility Principle:
 *     ✓ Cada clase tiene una única responsabilidad
 *     ✓ Controllers: Solo manejan HTTP
 *     ✓ Services: Solo lógica de negocio
 *     ✓ Repositories: Solo acceso a datos
 * 
 * O - Open/Closed Principle:
 *     ✓ Interfaces permiten extensión sin modificación
 *     ✓ Nuevas implementaciones no cambian código existente
 * 
 * L - Liskov Substitution Principle:
 *     ✓ Implementaciones pueden sustituir interfaces
 *     ✓ VenueServiceImpl sustituye a IVenueService
 * 
 * I - Interface Segregation Principle:
 *     ✓ Interfaces específicas para cada entidad
 *     ✓ IVenueService e IEventService separadas
 * 
 * D - Dependency Inversion Principle:
 *     ✓ Dependencias de abstracciones, no implementaciones
 *     ✓ Controllers dependen de IService
 *     ✓ Services dependen de Repository (interfaz)
 * 
 * 
 * TASK 1 - COMPLETADA ✅:
 * ✓ Entidades JPA creadas (EventEntity, VenueEntity)
 * ✓ Repositorios JPA implementados (EventRepository, VenueRepository)
 * ✓ Servicios migrados a base de datos
 * ✓ Relaciones y constraints validados
 * 
 * TASK 2 - COMPLETADA ✅:
 * ✓ Validaciones aplicadas (@Valid, @NotBlank, @Size, @Future, etc.)
 * ✓ Mensajes de error descriptivos implementados
 * ✓ Validación de duplicados en nombres de eventos
 * ✓ GlobalExceptionHandler con manejo coherente de errores
 * ✓ Respuestas HTTP correctas (400, 404, 409)
 */