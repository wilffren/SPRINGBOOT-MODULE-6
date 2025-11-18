package com.example.infraestructura.config;

import com.example.aplicacion.usecase.UsuarioService;
import com.example.dominio.ports.out.UsuarioRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de beans de Spring
 * Aquí se conectan los puertos con sus adaptadores
 */
@Configuration
public class ApplicationConfig {

    /**
     * Define el UsuarioService como bean de Spring
     * Inyecta el puerto de salida (implementado por UsuarioJpaAdapter)
     */
    @Bean
    public UsuarioService usuarioService(UsuarioRepositoryPort usuarioRepositoryPort) {
        return new UsuarioService(usuarioRepositoryPort);
    }
}