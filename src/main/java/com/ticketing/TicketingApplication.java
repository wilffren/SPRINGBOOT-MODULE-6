package com.ticketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TicketingApplication {
    
  
    public static void main(String[] args) {
        SpringApplication.run(TicketingApplication.class, args);
        
        System.out.println("\n===========================================");
        System.out.println("✓ Ticketing API iniciada correctamente");
        System.out.println("===========================================");
        System.out.println("📚 Swagger UI: http://localhost:8080/api/swagger-ui.html");
        System.out.println("📖 API Docs: http://localhost:8080/api/v3/api-docs");
        System.out.println("===========================================\n");
    }
}