package com.eventcatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para enviar datos de lugares al cliente.
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Solo maneja la transferencia de datos de salida
 * - OCP (Open/Closed): Puede extenderse sin modificar código existente
 * 
 * Este DTO evita exponer la entidad completa y permite controlar
 * exactamente qué información se envía al cliente.
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueResponseDTO {

    /**
     * ID único del lugar
     */
    private Long id;

    /**
     * Nombre del lugar
     */
    private String name;

    /**
     * Dirección completa
     */
    private String address;

    /**
     * Ciudad
     */
    private String city;

    /**
     * Capacidad máxima
     */
    private Integer capacity;

    /**
     * Descripción del lugar
     */
    private String description;

    /**
     * Fecha de creación del registro
     */
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización
     */
    private LocalDateTime updatedAt;

    /**
     * Número de eventos asociados al lugar (campo calculado)
     * Útil para mostrar en listados sin traer todos los eventos
     */
    private Integer eventCount;
}