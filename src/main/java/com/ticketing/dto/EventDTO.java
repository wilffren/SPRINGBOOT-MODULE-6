package com.ticketing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferir datos de Eventos entre capas.
 * 
 * Representa un evento que puede ser gestionado en el sistema.
 * Incluye validaciones para garantizar la integridad de los datos.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {
    
    /**
     * Identificador único del evento.
     * Se genera automáticamente en el servicio.
     */
    private Long id;
    
    /**
     * Nombre del evento.
     * No puede estar vacío y debe tener entre 3 y 100 caracteres.
     */
    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;
    
    /**
     * Descripción detallada del evento.
     */
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String description;
    
    /**
     * Fecha y hora del evento.
     */
    @NotNull(message = "La fecha del evento es obligatoria")
    private LocalDateTime eventDate;
    
    /**
     * Identificador del venue donde se realizará el evento.
     */
    @NotNull(message = "El ID del venue es obligatorio")
    @Positive(message = "El ID del venue debe ser positivo")
    private Long venueId;
    
    /**
     * Capacidad total del evento.
     */
    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser mayor a 0")
    private Integer capacity;
    
    /**
     * Precio del boleto del evento.
     */
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double price;
    
    /**
     * Categoría del evento (concierto, deportes, teatro, etc.).
     */
    @NotBlank(message = "La categoría es obligatoria")
    private String category;
    
    /**
     * Indica si el evento está activo o no.
     */
    private Boolean active;
}