package com.eventcatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para enviar datos de eventos al cliente.
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Solo maneja la transferencia de datos de salida
 * - OCP (Open/Closed): Puede extenderse sin modificar código existente
 * 
 * Este DTO incluye información del lugar (venue) de forma anidada
 * para evitar problemas de lazy loading y mejorar la experiencia del cliente.
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponseDTO {

    /**
     * ID único del evento
     */
    private Long id;

    /**
     * Nombre del evento
     */
    private String name;

    /**
     * Descripción del evento
     */
    private String description;

    /**
     * Categoría del evento
     */
    private String category;

    /**
     * Ciudad donde se realiza
     */
    private String city;

    /**
     * Fecha y hora del evento
     */
    private LocalDateTime eventDate;

    /**
     * Precio de la entrada
     */
    private BigDecimal price;

    /**
     * Entradas disponibles
     */
    private Integer availableTickets;

    /**
     * URL de la imagen
     */
    private String imageUrl;

    /**
     * Estado del evento
     */
    private String status;

    /**
     * Información del lugar (venue) de forma resumida
     */
    private VenueSummaryDTO venue;

    /**
     * Fecha de creación del registro
     */
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización
     */
    private LocalDateTime updatedAt;

    /**
     * DTO anidado para información resumida del lugar.
     * Evita enviar toda la información del venue si no es necesaria.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VenueSummaryDTO {
        private Long id;
        private String name;
        private String address;
        private String city;
        private Integer capacity;
    }
}