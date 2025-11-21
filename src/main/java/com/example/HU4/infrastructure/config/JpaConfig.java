package com.example.HU4.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * TASK 3: Configuración de JPA y Transacciones
 * Habilita gestión declarativa de transacciones
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.example.HU4.infrastructure.repositories")
@EnableTransactionManagement
public class JpaConfig {
    // La configuración se maneja principalmente en application.yml
    // Esta clase habilita explícitamente JPA y transacciones
}