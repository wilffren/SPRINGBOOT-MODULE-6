package com.example.HU4.infrastructure.repositories;

import com.example.HU4.infrastructure.entities.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para VenueEntity
 * Extiende JpaSpecificationExecutor para queries dinámicas
 */
@Repository
public interface JpaVenueRepository extends JpaRepository<VenueEntity, Long>, JpaSpecificationExecutor<VenueEntity> {

    /**
     * Buscar venues por capacidad mínima
     * Método derivado de Spring Data JPA
     */
    List<VenueEntity> findByCapacityGreaterThanEqual(Integer capacity);

    /**
     * Buscar venues por ubicación (búsqueda parcial)
     * Método derivado con LIKE
     */
    List<VenueEntity> findByLocationContainingIgnoreCase(String location);

    /**
     * Buscar venues por nombre (búsqueda parcial)
     */
    List<VenueEntity> findByNameContainingIgnoreCase(String name);

    /**
     * Buscar venues con capacidad en un rango específico
     */
    @Query("SELECT v FROM VenueEntity v WHERE v.capacity BETWEEN :minCapacity AND :maxCapacity ORDER BY v.capacity")
    List<VenueEntity> findByCapacityRange(@Param("minCapacity") Integer minCapacity,
            @Param("maxCapacity") Integer maxCapacity);
}
