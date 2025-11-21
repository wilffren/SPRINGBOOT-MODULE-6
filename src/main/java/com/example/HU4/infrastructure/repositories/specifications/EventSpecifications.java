package com.example.HU4.infrastructure.repositories.specifications;

import com.example.HU4.domain.model.EventStatus;
import com.example.HU4.infrastructure.entities.CategoryEntity;
import com.example.HU4.infrastructure.entities.EventEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Specifications para Event
 * Permite construir queries dinámicas combinando múltiples filtros
 * Resuelve problemas N+1 usando join fetch cuando es necesario
 */
public class EventSpecifications {

    /**
     * Filtrar eventos por venue ID
     */
    public static Specification<EventEntity> hasVenue(Long venueId) {
        return (root, query, criteriaBuilder) -> {
            if (venueId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("venue").get("id"), venueId);
        };
    }

    /**
     * Filtrar eventos por estado
     */
    public static Specification<EventEntity> hasStatus(EventStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    /**
     * Filtrar eventos que inician después de una fecha
     */
    public static Specification<EventEntity> startDateAfter(LocalDateTime startDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate);
        };
    }

    /**
     * Filtrar eventos que terminan antes de una fecha
     */
    public static Specification<EventEntity> endDateBefore(LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (endDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate);
        };
    }

    /**
     * Filtrar eventos en un rango de fechas específico
     */
    public static Specification<EventEntity> startDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null || endDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.between(root.get("startDate"), startDate, endDate);
        };
    }

    /**
     * Filtrar eventos por categoría (nombre de categoría)
     * Usa JOIN para evitar N+1
     */
    public static Specification<EventEntity> hasCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null || categoryName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // Join con categories para buscar por nombre
            Join<EventEntity, CategoryEntity> categoryJoin = root.join("categories", JoinType.INNER);
            return criteriaBuilder.equal(categoryJoin.get("name"), categoryName);
        };
    }

    /**
     * Filtrar eventos cuyo nombre contenga el texto dado (búsqueda parcial)
     */
    public static Specification<EventEntity> nameContains(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%");
        };
    }

    /**
     * Filtrar eventos de un venue en un rango de fechas
     * Specification compuesta combinando múltiples filtros
     */
    public static Specification<EventEntity> venueAndDateRange(Long venueId, LocalDateTime startDate,
            LocalDateTime endDate) {
        return Specification.where(hasVenue(venueId))
                .and(startDateAfter(startDate))
                .and(endDateBefore(endDate));
    }
}
