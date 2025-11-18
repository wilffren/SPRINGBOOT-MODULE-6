package com.example.infraestructura.adapters.out.jpa.repository;

import com.example.infraestructura.adapters.out.jpa.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA de Spring Data
 * Pertenece SOLO a la capa de infraestructura
 */
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByUsername(String username);
}