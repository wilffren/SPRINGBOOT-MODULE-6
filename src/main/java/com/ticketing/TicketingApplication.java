package com.ticketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Ticketing API.
 * 
 * Punto de entrada de la aplicación Spring Boot.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
@SpringBootApplication
public class TicketingApplication {
    
    /**
     * Método principal que inicia la aplicación.
     * 
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(TicketingApplication.class, args);
        
        System.out.println("\n===========================================");
        System.out.println("✓ Ticketing API iniciada correctamente");
        System.out.println("===========================================");
        System.out.println("📍 API Base URL: http://localhost:8080/api");
        System.out.println("📚 Swagger UI: http://localhost:8080/api/swagger-ui.html");
        System.out.println("📖 API Docs: http://localhost:8080/api/v3/api-docs");
        System.out.println("===========================================\n");
    }
}