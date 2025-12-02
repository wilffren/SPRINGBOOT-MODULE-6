package com.eventcatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración global de CORS (Cross-Origin Resource Sharing).
 * 
 * Permite que el frontend Angular (http://localhost:4200) pueda
 * realizar peticiones al backend Spring Boot (http://localhost:8080).
 * 
 * CORS es un mecanismo de seguridad de los navegadores que restringe
 * las peticiones HTTP entre diferentes orígenes (dominios).
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Solo maneja la configuración de CORS
 * - OCP (Open/Closed): Puede extenderse agregando nuevos orígenes sin modificar el código
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Configuration
public class CorsConfig {

    /**
     * Define y configura el filtro CORS para toda la aplicación.
     * 
     * @return CorsFilter configurado
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir credenciales (cookies, headers de autenticación)
        config.setAllowCredentials(true);
        
        // Orígenes permitidos (frontend Angular)
        // En producción, cambiar a la URL real del frontend
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "http://localhost:4201"  // Por si usas otro puerto
        ));
        
        // Métodos HTTP permitidos
        config.setAllowedMethods(Arrays.asList(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "PATCH",
            "OPTIONS"
        ));
        
        // Headers permitidos
        // "*" permite todos los headers
        config.setAllowedHeaders(Arrays.asList(
            "*"
        ));
        
        // Headers expuestos al cliente
        // Útil si necesitas headers personalizados en las respuestas
        config.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Total-Count"  // Ejemplo: header personalizado para paginación
        ));
        
        // Tiempo en segundos que el navegador puede cachear la respuesta preflight
        config.setMaxAge(3600L);
        
        // Aplicar configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}

/**
 * NOTAS IMPORTANTES:
 * 
 * 1. DESARROLLO vs PRODUCCIÓN:
 *    - En desarrollo: Usar localhost con puertos específicos
 *    - En producción: Cambiar a los dominios reales de tu frontend
 *      Ejemplo: "https://miapp.com", "https://www.miapp.com"
 * 
 * 2. SEGURIDAD:
 *    - NO uses "*" en allowedOrigins en producción
 *    - Solo permite los orígenes que realmente necesitas
 *    - Considera usar variables de entorno para los orígenes:
 *      config.setAllowedOrigins(Arrays.asList(System.getenv("FRONTEND_URL")));
 * 
 * 3. ALTERNATIVAS:
 *    - Puedes usar @CrossOrigin en cada controlador (menos mantenible)
 *    - Esta configuración global es más limpia y centralizada
 * 
 * 4. TROUBLESHOOTING:
 *    - Si ves errores de CORS en el navegador:
 *      a) Verifica que el origen esté en allowedOrigins
 *      b) Verifica que el método HTTP esté en allowedMethods
 *      c) Revisa la consola del navegador para detalles
 *      d) Usa herramientas como Postman para probar sin CORS
 */