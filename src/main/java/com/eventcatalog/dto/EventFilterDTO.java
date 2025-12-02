package com.eventcatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * DTO para encapsular los filtros de búsqueda de eventos.
 * 
 * TASK 3: Filtros opcionales por ciudad, categoría, fechaInicio.
 * 
 * Todos los campos son opcionales. Si un campo es null,
 * no se aplica ese filtro en la consulta.
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Solo encapsula parámetros de filtrado
 * - OCP (Open/Closed): Fácil agregar nuevos filtros sin modificar código existente
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFilterDTO {

    /**
     * Filtrar por ciudad.
     * Si es null, no se aplica este filtro.
     */
    private String city;

    /**
     * Filtrar por categoría.
     * Si es null, no se aplica este filtro.
     */
    private String category;

    /**
     * Filtrar por fecha de inicio (eventos a partir de esta fecha).
     * Si es null, no se aplica este filtro.
     * 
     * Formato esperado: yyyy-MM-dd'T'HH:mm:ss
     * Ejemplo: 2025-12-01T00:00:00
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    /**
     * Filtrar por fecha de fin (eventos hasta esta fecha).
     * Si es null, no se aplica este filtro.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    /**
     * Filtrar por precio mínimo.
     * Si es null, no se aplica este filtro.
     */
    private Double minPrice;

    /**
     * Filtrar por precio máximo.
     * Si es null, no se aplica este filtro.
     */
    private Double maxPrice;

    /**
     * Filtrar por disponibilidad de entradas.
     * Si es true, solo muestra eventos con entradas disponibles.
     * Si es false o null, no se aplica este filtro.
     */
    private Boolean hasAvailableTickets;

    /**
     * Filtrar por estado del evento.
     * Si es null, no se aplica este filtro.
     * Valores posibles: ACTIVE, CANCELLED, COMPLETED, POSTPONED
     */
    private String status;

    /**
     * Búsqueda de texto libre en nombre y descripción.
     * Si es null, no se aplica este filtro.
     */
    private String searchTerm;

    /**
     * Verifica si hay algún filtro aplicado.
     * 
     * @return true si al menos un filtro está presente
     */
    public boolean hasAnyFilter() {
        return city != null 
            || category != null 
            || startDate != null 
            || endDate != null
            || minPrice != null
            || maxPrice != null
            || hasAvailableTickets != null
            || status != null
            || searchTerm != null;
    }
}

/**
 * EJEMPLO DE USO:
 * 
 * URL con filtros:
 * GET /api/events?page=0&size=10&sort=eventDate,asc&city=Bogotá&category=Concierto&startDate=2025-12-01T00:00:00
 * 
 * En el controlador:
 * 
 * @GetMapping
 * public ResponseEntity<PageResponseDTO<EventResponseDTO>> findAll(
 *     @RequestParam(required = false) String city,
 *     @RequestParam(required = false) String category,
 *     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
 *     Pageable pageable
 * ) {
 *     EventFilterDTO filters = EventFilterDTO.builder()
 *         .city(city)
 *         .category(category)
 *         .startDate(startDate)
 *         .build();
 *     
 *     return eventService.findAllWithFilters(filters, pageable);
 * }
 */