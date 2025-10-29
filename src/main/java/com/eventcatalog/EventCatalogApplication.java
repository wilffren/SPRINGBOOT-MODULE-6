package com.eventcatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class EventCatalogApplication {

  
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

