package com.ticketing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para documentación de la API.
 * 
 * Define la información general de la API que se mostrará en Swagger UI.
 * Accesible en: http://localhost:8080/api/swagger-ui.html
 * 
 * @author Ticketing Team
 * @version 1.0
 */
@Configuration
public class OpenApiConfig {
    
    /**
     * Configura la información general de la API.
     * 
     * @return Objeto OpenAPI con la configuración
     */
    @Bean
    public OpenAPI ticketingOpenAPI() {
        // Servidor local de desarrollo
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080/api");
        devServer.setDescription("Servidor de Desarrollo");
        
        // Información de contacto
        Contact contact = new Contact();
        contact.setEmail("soporte@ticketing.com");
        contact.setName("Ticketing Team");
        contact.setUrl("https://www.ticketing.com");
        
        // Licencia
        License license = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");
        
        // Información general de la API
        Info info = new Info()
                .title("Ticketing API - Catálogo de Eventos y Venues")
                .version("1.0.0")
                .contact(contact)
                .description("API REST para gestión de eventos y venues (lugares/recintos). " +
                           "Esta es la primera versión del catálogo In-Memory " +
                           "con arquitectura por capas y principios SOLID.")
                .termsOfService("https://www.ticketing.com/terms")
                .license(license);
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}