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
            // Crear usuario de prueba y generar token automáticamente
            String testUsername = "admin";
            String testPassword = "admin123";

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

            System.out.println("\n" + "═".repeat(80));
            System.out.println("� HU5 EVENT MANAGEMENT API");
            System.out.println("═".repeat(80));

            System.out.println("\n📚 SWAGGER UI:");
            System.out.println("   🌐 http://localhost:8080/swagger-ui.html");

            System.out.println("\n🔑 TOKEN JWT AUTO-GENERADO:");
            System.out.println("   " + autoToken);
            System.out.println("\n   👤 Usuario: " + testUsername);
            System.out.println("   🔐 Password: " + testPassword);

            System.out.println("\n" + "═".repeat(80) + "\n");
        };
    }
}
