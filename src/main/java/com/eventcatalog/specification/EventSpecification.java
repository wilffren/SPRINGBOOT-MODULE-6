package com.eventcatalog.specification;

import com.eventcatalog.entity.EventEntity;
import com.eventcatalog.dto.EventFilterDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de utilidad para crear Specifications dinámicas de filtrado de eventos.
 * 
 * TASK 3: Implementa filtros opcionales combinables de manera flexible.
 * 
 * Specifications permite construir consultas JPA de forma programática y type-safe,
 * evitando SQL injection y permitiendo combinar múltiples filtros dinámicamente.
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Solo crea specifications para filtrado
 * - OCP (Open/Closed): Fácil agregar nuevas specifications sin modificar las existentes
 * - DIP (Dependency Inversion): Usa la abstracción Specification de Spring Data
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
public class EventSpecification {

    /**
     * Constructor privado para evitar instanciación.
     * Esta es una clase de utilidad con métodos estáticos.
     */
    private EventSpecification() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Crea una Specification que combina todos los filtros del DTO.
     * 
     * TASK 3: Filtros opcionales por ciudad, categoría, fechaInicio.
     * 
     * @param filters DTO con los filtros a aplicar
     * @return Specification combinada con todos los filtros
     */
    public static Specification<EventEntity> withFilters(EventFilterDTO filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por ciudad (case insensitive)
            if (filters.getCity() != null && !filters.getCity().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("city")),
                        "%" + filters.getCity().toLowerCase() + "%"
                    )
                );
            }

            // Filtro por categoría (case insensitive)
            if (filters.getCategory() != null && !filters.getCategory().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("category")),
                        "%" + filters.getCategory().toLowerCase() + "%"
                    )
                );
            }

            // Filtro por fecha de inicio (eventos a partir de esta fecha)
            if (filters.getStartDate() != null) {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("eventDate"),
                        filters.getStartDate()
                    )
                );
            }

            // Filtro por fecha de fin (eventos hasta esta fecha)
            if (filters.getEndDate() != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("eventDate"),
                        filters.getEndDate()
                    )
                );
            }

            // Filtro por precio mínimo
            if (filters.getMinPrice() != null) {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"),
                        filters.getMinPrice()
                    )
                );
            }

            // Filtro por precio máximo
            if (filters.getMaxPrice() != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"),
                        filters.getMaxPrice()
                    )
                );
            }

            // Filtro por disponibilidad de entradas
            if (filters.getHasAvailableTickets() != null && filters.getHasAvailableTickets()) {
                predicates.add(
                    criteriaBuilder.greaterThan(
                        root.get("availableTickets"),
                        0
                    )
                );
            }

            // Filtro por estado
            if (filters.getStatus() != null && !filters.getStatus().trim().isEmpty()) {
                try {
                    EventEntity.EventStatus status = EventEntity.EventStatus.valueOf(
                        filters.getStatus().toUpperCase()
                    );
                    predicates.add(
                        criteriaBuilder.equal(root.get("status"), status)
                    );
                } catch (IllegalArgumentException e) {
                    // Si el estado no es válido, ignorar este filtro
                }
            }

            // Filtro por búsqueda de texto en nombre o descripción
            if (filters.getSearchTerm() != null && !filters.getSearchTerm().trim().isEmpty()) {
                String searchPattern = "%" + filters.getSearchTerm().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    searchPattern
                );
                Predicate descriptionPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    searchPattern
                );
                predicates.add(criteriaBuilder.or(namePredicate, descriptionPredicate));
            }

            // Combinar todos los predicates con AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Specification para filtrar por ciudad.
     * 
     * @param city Ciudad a buscar
     * @return Specification de ciudad
     */
    public static Specification<EventEntity> hasCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null || city.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(
                criteriaBuilder.lower(root.get("city")),
                city.toLowerCase()
            );
        };
    }

    /**
     * Specification para filtrar por categoría.
     * 
     * @param category Categoría a buscar
     * @return Specification de categoría
     */
    public static Specification<EventEntity> hasCategory(String category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null || category.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(
                criteriaBuilder.lower(root.get("category")),
                category.toLowerCase()
            );
        };
    }

    /**
     * Specification para filtrar eventos futuros.
     * 
     * @return Specification de eventos futuros
     */
    public static Specification<EventEntity> isFuture() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThanOrEqualTo(
                root.get("eventDate"),
                LocalDateTime.now()
            );
    }

    /**
     * Specification para filtrar por estado ACTIVE.
     * 
     * @return Specification de eventos activos
     */
    public static Specification<EventEntity> isActive() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(
                root.get("status"),
                EventEntity.EventStatus.ACTIVE
            );
    }

    /**
     * Specification para filtrar eventos con entradas disponibles.
     * 
     * @return Specification de eventos con tickets disponibles
     */
    public static Specification<EventEntity> hasAvailableTickets() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThan(root.get("availableTickets"), 0);
    }

    /**
     * Specification para filtrar por rango de fechas.
     * 
     * @param startDate Fecha de inicio
     * @param endDate Fecha de fin
     * @return Specification de rango de fechas
     */
    public static Specification<EventEntity> hasDateBetween(
            LocalDateTime startDate, 
            LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(
                    root.get("eventDate"),
                    startDate,
                    endDate
                );
            } else if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("eventDate"),
                    startDate
                );
            } else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(
                    root.get("eventDate"),
                    endDate
                );
            }
            return criteriaBuilder.conjunction();
        };
    }

    /**
     * Specification para filtrar por rango de precios.
     * 
     * @param minPrice Precio mínimo
     * @param maxPrice Precio máximo
     * @return Specification de rango de precios
     */
    public static Specification<EventEntity> hasPriceBetween(
            Double minPrice, 
            Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(
                    root.get("price"),
                    minPrice,
                    maxPrice
                );
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("price"),
                    minPrice
                );
            } else if (maxPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(
                    root.get("price"),
                    maxPrice
                );
            }
            return criteriaBuilder.conjunction();
        };
    }
}

/**
 * EJEMPLO DE USO:
 * 
 * // En el servicio o repositorio
 * Specification<EventEntity> spec = EventSpecification.withFilters(filters);
 * Page<EventEntity> events = eventRepository.findAll(spec, pageable);
 * 
 * // O combinando specifications individuales:
 * Specification<EventEntity> spec = Specification
 *     .where(EventSpecification.hasCity("Bogotá"))
 *     .and(EventSpecification.hasCategory("Concierto"))
 *     .and(EventSpecification.isFuture())
 *     .and(EventSpecification.isActive());
 * 
 * Page<EventEntity> events = eventRepository.findAll(spec, pageable);
 */