package com.example.HU4.infrastructure.repositories;

import com.example.HU4.infrastructure.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para TaskEntity
 * TASK 2: Implementación de consultas optimizadas
 * - JPQL para queries personalizadas
 * - JpaSpecificationExecutor para filtros dinámicos
 * - @EntityGraph para resolver N+1
 */
@Repository
public interface TaskJpaRepository extends 
    JpaRepository<TaskEntity, Long>,
    JpaSpecificationExecutor<TaskEntity> {
    
    /**
     * TASK 2: Query básica con JPQL
     */
    @Query("SELECT t FROM TaskEntity t WHERE t.user.id = :userId")
    List<TaskEntity> findByUserId(@Param("userId") Long userId);
    
    /**
     * TASK 2: Resolver N+1 con @EntityGraph
     * Carga el usuario en la misma consulta (LEFT JOIN FETCH)
     */
    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT t FROM TaskEntity t WHERE t.user.id = :userId")
    List<TaskEntity> findByUserIdWithUser(@Param("userId") Long userId);
    
    /**
     * TASK 2: Query con JPQL - Filtro por estado
     */
    @Query("SELECT t FROM TaskEntity t WHERE t.status = :status")
    List<TaskEntity> findByStatus(@Param("status") String status);
    
    /**
     * TASK 2: Query con JPQL - Filtro por prioridad
     */
    @Query("SELECT t FROM TaskEntity t WHERE t.priority = :priority")
    List<TaskEntity> findByPriority(@Param("priority") String priority);
    
    /**
     * TASK 2: Query compleja con múltiples joins y filtros
     * Usa JOIN FETCH para evitar N+1
     */
    @Query("""
        SELECT DISTINCT t 
        FROM TaskEntity t 
        LEFT JOIN FETCH t.user u
        WHERE (:userId IS NULL OR t.user.id = :userId)
          AND (:status IS NULL OR t.status = :status)
          AND (:priority IS NULL OR t.priority = :priority)
          AND (:dueDate IS NULL OR t.dueDate = :dueDate)
        ORDER BY t.dueDate ASC, t.priority DESC
    """)
    List<TaskEntity> findByFilters(
        @Param("userId") Long userId,
        @Param("status") String status,
        @Param("priority") String priority,
        @Param("dueDate") LocalDate dueDate
    );
    
    /**
     * TASK 2: Consulta tareas vencidas con JPQL
     */
    @Query("""
        SELECT t FROM TaskEntity t 
        WHERE t.dueDate < CURRENT_DATE 
          AND t.status != 'COMPLETED'
    """)
    List<TaskEntity> findOverdueTasks();
    
    /**
     * TASK 2: Contar tareas por usuario
     */
    @Query("SELECT COUNT(t) FROM TaskEntity t WHERE t.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
}