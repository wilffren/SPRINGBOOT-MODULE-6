package com.example.HU4.infrastructure.repositories.specifications;

import com.example.HU4.infrastructure.entities.TaskEntity;
import com.example.HU4.infrastructure.entities.UserEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * TASK 2: Specifications para consultas dinámicas
 * Permite construir queries complejas de forma programática
 * Solución elegante para filtros opcionales
 */
public class TaskSpecifications {
    
    /**
     * Filtro por ID de usuario
     */
    public static Specification<TaskEntity> hasUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) return cb.conjunction();
            
            // Join con user para evitar N+1 si es necesario
            Join<TaskEntity, UserEntity> userJoin = root.join("user", JoinType.LEFT);
            return cb.equal(userJoin.get("id"), userId);
        };
    }
    
    /**
     * Filtro por estado
     */
    public static Specification<TaskEntity> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isBlank()) return cb.conjunction();
            return cb.equal(root.get("status"), status);
        };
    }
    
    /**
     * Filtro por prioridad
     */
    public static Specification<TaskEntity> hasPriority(String priority) {
        return (root, query, cb) -> {
            if (priority == null || priority.isBlank()) return cb.conjunction();
            return cb.equal(root.get("priority"), priority);
        };
    }
    
    /**
     * Filtro por fecha de vencimiento
     */
    public static Specification<TaskEntity> hasDueDate(LocalDate dueDate) {
        return (root, query, cb) -> {
            if (dueDate == null) return cb.conjunction();
            return cb.equal(root.get("dueDate"), dueDate);
        };
    }
    
    /**
     * Filtro para tareas vencidas
     */
    public static Specification<TaskEntity> isOverdue() {
        return (root, query, cb) -> cb.and(
            cb.lessThan(root.get("dueDate"), LocalDate.now()),
            cb.notEqual(root.get("status"), "COMPLETED")
        );
    }
    
    /**
     * Filtro para tareas completadas
     */
    public static Specification<TaskEntity> isCompleted() {
        return (root, query, cb) -> 
            cb.equal(root.get("status"), "COMPLETED");
    }
    
    /**
     * Filtro por título (búsqueda parcial)
     */
    public static Specification<TaskEntity> titleContains(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return cb.conjunction();
            return cb.like(
                cb.lower(root.get("title")), 
                "%" + keyword.toLowerCase() + "%"
            );
        };
    }
    
    /**
     * Combina múltiples filtros
     * Ejemplo de uso en servicio:
     * Specification<TaskEntity> spec = Specification
     *     .where(hasUserId(userId))
     *     .and(hasStatus(status))
     *     .and(hasPriority(priority));
     */
    public static Specification<TaskEntity> withFilters(
        Long userId, 
        String status, 
        String priority, 
        LocalDate dueDate
    ) {
        return Specification
            .where(hasUserId(userId))
            .and(hasStatus(status))
            .and(hasPriority(priority))
            .and(hasDueDate(dueDate));
    }
}