package com.example.HU4.infrastructure.repositories;

import com.example.HU4.domain.model.EventStatus;
import com.example.HU4.infrastructure.entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para EventEntity
 * Extiende JpaSpecificationExecutor para permitir queries dinámicas con
 * Specifications
 * Implementa queries JPQL optimizadas y @EntityGraph para evitar N+1
 */
@Repository
public interface JpaEventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

        /**
         * Buscar eventos por venue ID
         * Usa @EntityGraph para cargar venue junto con eventos (evita N+1)
         */
        @EntityGraph(attributePaths = { "venue" })
        List<EventEntity> findByVenueId(Long venueId);

        /**
         * Buscar eventos por estado
         * 
         * @EntityGraph carga venue automáticamente
         */
        @EntityGraph(attributePaths = { "venue" })
        List<EventEntity> findByStatus(EventStatus status);

        /**
         * Buscar eventos en un rango de fechas
         * Usa JPQL con join fetch para optimizar carga de venue
         */
        @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue v WHERE e.startDate >= :startDate AND e.endDate <= :endDate")
        List<EventEntity> findByDateRangeBetween(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        /**
         * Buscar eventos activos en un rango de fechas para un venue específico
         * Query más compleja con múltiples condiciones
         */
        @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue v " +
                        "WHERE e.venue.id = :venueId " +
                        "AND e.status = :status " +
                        "AND e.startDate >= :startDate " +
                        "AND e.endDate <= :endDate")
        List<EventEntity> findActiveEventsByVenueAndDateRange(
                        @Param("venueId") Long venueId,
                        @Param("status") EventStatus status,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        /**
         * Sobrescribir findAll para incluir @EntityGraph y evitar N+1
         */
        @Override
        @EntityGraph(attributePaths = { "venue" })
        List<EventEntity> findAll();

        /**
         * Buscar eventos por categoría
         */
        @Query("SELECT DISTINCT e FROM EventEntity e " +
                        "JOIN FETCH e.venue " +
                        "LEFT JOIN FETCH e.categories c " +
                        "WHERE c.name = :categoryName")
        List<EventEntity> findByCategoryName(@Param("categoryName") String categoryName);
}
