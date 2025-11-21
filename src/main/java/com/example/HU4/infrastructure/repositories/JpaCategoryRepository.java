package com.example.HU4.infrastructure.repositories;

import com.example.HU4.infrastructure.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para CategoryEntity
 */
@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {

    /**
     * Buscar categoría por nombre
     */
    Optional<CategoryEntity> findByName(String name);

    /**
     * Verificar si existe una categoría con el nombre dado
     */
    boolean existsByName(String name);
}
