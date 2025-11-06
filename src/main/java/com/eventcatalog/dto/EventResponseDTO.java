package com.eventcatalog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
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
 * - SRP (Single Responsibility): Solo maneja la transferencia de datos de salida.
 * - OCP (Open/Closed): Puede extenderse sin modificar código existente.
 *
 * Este DTO incluye información del lugar (venue) de forma anidada
 * para evitar problemas de lazy loading y mejorar la experiencia del cliente.
 *
 * @author Event Catalog Team
 * @version 1.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponseDTO {

    /**
     * ID único del evento.
     */
    private Long id;

    /**
     * Nombre del evento.
     */
    private String name;

    /**
     * Descripción del evento.
     */
    private String description;

    /**
     * Categoría del evento.
     */
    private String category;

    /**
     * Ciudad donde se realiza.
     */
    private String city;

    /**
     * Fecha y hora del evento.
     * Compatible con inputs HTML5 de tipo datetime-local (yyyy-MM-dd'T'HH:mm)
     * y con serialización/deserialización JSON.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime eventDate;

    /**
     * Precio de la entrada.
     */
    private BigDecimal price;

    /**
     * Entradas disponibles.
     */
    private Integer availableTickets;

    /**
     * URL de la imagen.
     */
    private String imageUrl;

    /**
     * Estado del evento.
     */
    private String status;

    /**
     * Información del lugar (venue) de forma resumida.
     */
    private VenueSummaryDTO venue;

    /**
     * Fecha de creación del registro.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
