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
            System.out.println("🚀 APLICACIÓN HU5 - EVENT MANAGEMENT");
            System.out.println("=".repeat(80));
            System.out.println("\n📍 SERVIDOR: http://localhost:8080");
            System.out.println("\n" + "─".repeat(80));
            System.out.println("🔓 ENDPOINTS PÚBLICOS (No requieren autenticación):");
            System.out.println("─".repeat(80));

            System.out.println("\n1️⃣  REGISTRAR USUARIO:");
            System.out.println("   POST http://localhost:8080/api/auth/register");
            System.out.println("   Body: {\"username\":\"admin\",\"password\":\"admin123\"}");
            System.out.println("   🔑 El token JWT REAL se mostrará automáticamente en esta terminal");

            System.out.println("\n2️⃣  LOGIN:");
            System.out.println("   POST http://localhost:8080/api/auth/login");
            System.out.println("   Body: {\"username\":\"admin\",\"password\":\"admin123\"}");
            System.out.println("   🔑 El token JWT REAL se mostrará automáticamente en esta terminal");

            System.out.println("\n" + "─".repeat(80));
            System.out.println("🔐 ENDPOINTS PROTEGIDOS (Requieren JWT Token):");
            System.out.println("─".repeat(80));

            System.out.println("\n3️⃣  CREAR VENUE:");
            System.out.println("   POST http://localhost:8080/api/venues");
            System.out.println("   Header: Authorization: Bearer {TU_TOKEN_GENERADO}");
            System.out.println(
                    "   Body: {\"name\":\"Teatro\",\"location\":\"Centro\",\"capacity\":500,\"description\":\"Desc\"}");

            System.out.println("\n4️⃣  LISTAR VENUES:");
            System.out.println("   GET http://localhost:8080/api/venues");
            System.out.println("   Header: Authorization: Bearer {TU_TOKEN_GENERADO}");

            System.out.println("\n5️⃣  CREAR EVENTO:");
            System.out.println("   POST http://localhost:8080/api/events");
            System.out.println("   Header: Authorization: Bearer {TU_TOKEN_GENERADO}");

            System.out.println("\n" + "─".repeat(80));
            System.out.println("🔧 EJEMPLO CON CURL:");
            System.out.println("─".repeat(80));
            System.out.println("curl -X POST http://localhost:8080/api/auth/register \\");
            System.out.println("  -H \"Content-Type: application/json\" \\");
            System.out.println("  -d '{\"username\":\"admin\",\"password\":\"admin123\"}'");

            System.out.println("\n" + "=".repeat(80));
            System.out.println("✅ Aplicación lista - Los tokens JWT REALES aparecerán aquí cuando se generen");
            System.out.println("=".repeat(80) + "\n");
        };
    }
}
