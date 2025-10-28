package com.ticketing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing).
 * 
 * Permite que el frontend Angular (u otros clientes) puedan
 * realizar peticiones al backend desde diferentes orígenes.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
@Configuration
public class CorsConfig {
    
    /**
     * Configura el filtro CORS para toda la aplicación.
     * 
     * @return CorsFilter configurado
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir credenciales (cookies, headers de autorización)
        config.setAllowCredentials(true);
        
        // Permitir el origen del frontend Angular en desarrollo
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        
        // Permitir todos los headers
        config.addAllowedHeader("*");
        
        // Permitir métodos HTTP comunes
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Headers expuestos al cliente
        config.setExposedHeaders(Arrays.asList("Authorization"));
        
        // Aplicar configuración a todos los endpoints
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}