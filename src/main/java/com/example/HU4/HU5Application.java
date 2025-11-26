package com.example.HU4;

import com.example.HU4.domain.model.User;
import com.example.HU4.domain.ports.out.UserRepositoryPort;
import com.example.HU4.infrastructure.security.JwtService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@SpringBootApplication
public class HU5Application {
    public static void main(String[] args) {
        SpringApplication.run(HU5Application.class, args);
    }

    @Bean
    public CommandLineRunner displayEndpointsAndGenerateToken(
            UserRepositoryPort userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        return args -> {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("🚀 APLICACIÓN HU5 - EVENT MANAGEMENT");
            System.out.println("=".repeat(80));
            System.out.println("\n📍 SERVIDOR: http://localhost:8080");

            // Crear usuario de prueba y generar token automáticamente
            String testUsername = "admin";
            String testPassword = "admin123";

            // Verificar si el usuario ya existe
            if (!userRepository.existsByUsername(testUsername)) {
                User testUser = User.builder()
                        .username(testUsername)
                        .password(passwordEncoder.encode(testPassword))
                        .role("USER")
                        .build();
                userRepository.save(testUser);
            }

            // Generar token para usuario de prueba
            User user = userRepository.findByUsername(testUsername).orElseThrow();
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.emptyList());
            String autoToken = jwtService.generateToken(userDetails);

            System.out.println("\n" + "━".repeat(80));
            System.out.println("🔑 TOKEN JWT AUTO-GENERADO AL INICIO");
            System.out.println("━".repeat(80));
            System.out.println("👤 Usuario de prueba: " + testUsername);
            System.out.println("🔐 Contraseña: " + testPassword);
            System.out.println("\n🎫 TOKEN COMPLETO:");
            System.out.println("   " + autoToken);
            System.out.println("\n💡 Úsalo así:");
            System.out.println("   Authorization: Bearer " + autoToken);
            System.out.println("━".repeat(80));

            System.out.println("\n" + "─".repeat(80));
            System.out.println("🔓 ENDPOINTS PÚBLICOS (No requieren autenticación):");
            System.out.println("─".repeat(80));

            System.out.println("\n1️⃣  REGISTRAR USUARIO:");
            System.out.println("   POST http://localhost:8080/api/auth/register");
            System.out.println("   Body: {\"username\":\"nuevo\",\"password\":\"pass123\"}");

            System.out.println("\n2️⃣  LOGIN:");
            System.out.println("   POST http://localhost:8080/api/auth/login");
            System.out.println("   Body: {\"username\":\"admin\",\"password\":\"admin123\"}");

            System.out.println("\n" + "─".repeat(80));
            System.out.println("🔐 ENDPOINTS PROTEGIDOS (Usa el token de arriba):");
            System.out.println("─".repeat(80));

            System.out.println("\n3️⃣  CREAR VENUE:");
            System.out.println("   POST http://localhost:8080/api/venues");
            System.out.println("   Header: Authorization: Bearer " + autoToken.substring(0, 30) + "...");
            System.out.println(
                    "   Body: {\"name\":\"Teatro\",\"location\":\"Centro\",\"capacity\":500,\"description\":\"Desc\"}");

            System.out.println("\n4️⃣  LISTAR VENUES:");
            System.out.println("   GET http://localhost:8080/api/venues");
            System.out.println("   Header: Authorization: Bearer " + autoToken.substring(0, 30) + "...");

            System.out.println("\n5️⃣  CREAR EVENTO:");
            System.out.println("   POST http://localhost:8080/api/events");
            System.out.println("   Header: Authorization: Bearer " + autoToken.substring(0, 30) + "...");

            System.out.println("\n" + "─".repeat(80));
            System.out.println("🔧 PRUEBA RÁPIDA CON CURL:");
            System.out.println("─".repeat(80));
            System.out.println("# Listar venues con el token auto-generado:");
            System.out.println("curl -X GET http://localhost:8080/api/venues \\");
            System.out.println("  -H \"Authorization: Bearer " + autoToken + "\"");

            System.out.println("\n" + "=".repeat(80));
            System.out.println("✅ Aplicación lista - Token generado y listo para usar");
            System.out.println("=".repeat(80) + "\n");
        };
    }
}
