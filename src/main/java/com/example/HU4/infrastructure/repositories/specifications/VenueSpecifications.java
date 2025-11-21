package com.example.HU4.infrastructure.repositories.specifications;

import com.example.HU4.infrastructure.entities.EventEntity;
import com.example.HU4.infrastructure.entities.VenueEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Specifications para Venue
 * Permite construir queries dinámicas para búsqueda de venues
 */
public class VenueSpecifications {

    /**
     * Filtrar venues con capacidad mayor o igual al valor especificado
     */
    public static Specification<VenueEntity> capacityGreaterThanOrEqual(Integer capacity) {
        return (root, query, criteriaBuilder) -> {
            if (capacity == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("capacity"), capacity);
        };
    }

    /**
     * Filtrar venues por ubicación (búsqueda parcial, case-insensitive)
     */
    public static Specification<VenueEntity> locationContains(String location) {
        return (root, query, criteriaBuilder) -> {
            if (location == null || location.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("location")),
                    "%" + location.toLowerCase() + "%");
        };
    }

    /**
     * Filtrar venues por nombre (búsqueda parcial, case-insensitive)
     */
    public static Specification<VenueEntity> nameContains(String name) {
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
     * Filtrar venues con capacidad en un rango
     */
    public static Specification<VenueEntity> capacityBetween(Integer minCapacity, Integer maxCapacity) {
        return (root, query, criteriaBuilder) -> {
            if (minCapacity == null || maxCapacity == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.between(root.get("capacity"), minCapacity, maxCapacity);
        };
    }

    /**
     * Filtrar venues que tienen eventos en un rango de fechas específico
     * Usa JOIN para buscar a través de la relación
     */
    public static Specification<VenueEntity> hasEventsInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null || endDate == null) {
                return criteriaBuilder.conjunction();
            }

            // Join con events para filtrar por fechas de eventos
            Join<VenueEntity, EventEntity> eventJoin = root.join("events", JoinType.INNER);

            return criteriaBuilder.and(
                    criteriaBuilder.greaterThanOrEqualTo(eventJoin.get("startDate"), startDate),
                    criteriaBuilder.lessThanOrEqualTo(eventJoin.get("endDate"), endDate));
        };
    }
}
