package com.example.HU4;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HU5Application {
    public static void main(String[] args) {
        SpringApplication.run(HU5Application.class, args);
    }

    @Bean
    public CommandLineRunner displayEndpoints() {
        return args -> {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("🚀 APLICACIÓN INICIADA CORRECTAMENTE");
            System.out.println("=".repeat(80));
            System.out.println("\n📍 SERVIDOR: http://localhost:8080");
            System.out.println("\n" + "─".repeat(80));
            System.out.println("🔓 ENDPOINTS PÚBLICOS (No requieren autenticación):");
            System.out.println("─".repeat(80));

            System.out.println("\n1️⃣  REGISTRAR USUARIO:");
            System.out.println("   POST http://localhost:8080/api/auth/register");
            System.out.println("   Body: {\"username\":\"admin\",\"password\":\"admin123\"}");
            System.out.println("   👉 Respuesta: {\"token\":\"eyJhbGc...\"}  ← COPIA ESTE TOKEN");

            System.out.println("\n2️⃣  LOGIN:");
            System.out.println("   POST http://localhost:8080/api/auth/login");
            System.out.println("   Body: {\"username\":\"admin\",\"password\":\"admin123\"}");
            System.out.println("   👉 Respuesta: {\"token\":\"eyJhbGc...\"}  ← COPIA ESTE TOKEN");

            System.out.println("\n" + "─".repeat(80));
            System.out.println("🔐 ENDPOINTS PROTEGIDOS (Requieren JWT Token):");
            System.out.println("─".repeat(80));

            System.out.println("\n3️⃣  CREAR EVENTO:");
            System.out.println("   POST http://localhost:8080/api/events");
            System.out.println("   Header: Authorization: Bearer TU_TOKEN_AQUI");
            System.out.println("   Body: {");
            System.out.println("     \"name\":\"Concierto Rock\",");
            System.out.println("     \"description\":\"Gran concierto\",");
            System.out.println("     \"startDate\":\"2024-12-25T20:00:00\",");
            System.out.println("     \"endDate\":\"2024-12-25T23:00:00\",");
            System.out.println("     \"status\":\"SCHEDULED\",");
            System.out.println("     \"venueId\":1");
            System.out.println("   }");

            System.out.println("\n4️⃣  CREAR VENUE:");
            System.out.println("   POST http://localhost:8080/api/venues");
            System.out.println("   Header: Authorization: Bearer TU_TOKEN_AQUI");
            System.out.println("   Body: {");
            System.out.println("     \"name\":\"Teatro Principal\",");
            System.out.println("     \"location\":\"Centro\",");
            System.out.println("     \"capacity\":500,");
            System.out.println("     \"description\":\"Teatro principal\"");
            System.out.println("   }");

            System.out.println("\n5️⃣  LISTAR VENUES:");
            System.out.println("   GET http://localhost:8080/api/venues");
            System.out.println("   Header: Authorization: Bearer TU_TOKEN_AQUI");

            System.out.println("\n" + "─".repeat(80));
            System.out.println("💡 CÓMO USAR EL TOKEN:");
            System.out.println("─".repeat(80));
            System.out.println("1. Registra o logea un usuario (endpoints 1 o 2)");
            System.out.println("2. COPIA el token de la respuesta (empieza con 'eyJ...')");
            System.out.println("3. En Postman/curl, agrega el header:");
            System.out.println("   Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...");
            System.out.println("   (reemplaza 'eyJhbGc...' con tu token completo)");

            System.out.println("\n" + "─".repeat(80));
            System.out.println("🔧 EJEMPLO CON CURL:");
            System.out.println("─".repeat(80));
            System.out.println("# 1. Registrar:");
            System.out.println("curl -X POST http://localhost:8080/api/auth/register \\");
            System.out.println("  -H \"Content-Type: application/json\" \\");
            System.out.println("  -d '{\"username\":\"admin\",\"password\":\"admin123\"}'");
            System.out.println("\n# 2. Usar el token (copia el token de la respuesta anterior):");
            System.out.println("curl -X GET http://localhost:8080/api/venues \\");
            System.out.println("  -H \"Authorization: Bearer TU_TOKEN_AQUI\"");

            System.out.println("\n" + "=".repeat(80));
            System.out.println("✅ Aplicación lista para recibir peticiones");
            System.out.println("=".repeat(80) + "\n");
        };
    }
}
