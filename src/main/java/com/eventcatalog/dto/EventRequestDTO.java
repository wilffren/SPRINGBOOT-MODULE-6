package com.eventcatalog.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para recibir datos de creación/actualización de eventos.
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Solo maneja la transferencia de datos de entrada
 * - DIP (Dependency Inversion): No depende de la entidad directamente
 * 
 * TASK 2: Implementa validaciones exhaustivas con Bean Validation
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestDTO {

    /**
     * Nombre del evento.
     * Validaciones:
     * - No puede ser nulo o vacío
     * - Debe tener entre 5 y 150 caracteres
     * - No puede contener solo espacios en blanco
     */
    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(min = 5, max = 150, message = "El nombre debe tener entre 5 y 150 caracteres")
    private String name;

    /**
     * Descripción detallada del evento.
     * Validaciones:
     * - No puede ser nula o vacía
     * - Debe tener entre 20 y 1000 caracteres
     */
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 20, max = 1000, message = "La descripción debe tener entre 20 y 1000 caracteres")
    private String description;

    /**
     * Categoría del evento.
     * Validaciones:
     * - No puede ser nula o vacía
     * - Debe tener entre 3 y 50 caracteres
     * - Ejemplos: "Concierto", "Teatro", "Deporte", "Conferencia"
     */
    @NotBlank(message = "La categoría es obligatoria")
    @Size(min = 3, max = 50, message = "La categoría debe tener entre 3 y 50 caracteres")
    private String category;

    /**
     * Ciudad donde se realizará el evento.
     * Validaciones:
     * - No puede ser nula o vacía
     * - Debe tener entre 2 y 50 caracteres
     */
    @NotBlank(message = "La ciudad es obligatoria")
    @Size(min = 2, max = 50, message = "La ciudad debe tener entre 2 y 50 caracteres")
    private String city;

    /**
     * Fecha y hora del evento.
     * Validaciones:
     * - No puede ser nula
     * - Debe ser una fecha futura
     * 
     * Nota: @Future valida que la fecha sea estrictamente futura
     * @FutureOrPresent permitiría la fecha actual
     */
    @NotNull(message = "La fecha del evento es obligatoria")
    @Future(message = "La fecha del evento debe ser futura")
    private LocalDateTime eventDate;

    /**
     * Precio de la entrada.
     * Validaciones:
     * - No puede ser nulo
     * - Debe ser un valor positivo
     * - Mínimo 0.01 (no puede ser gratis en este sistema)
     * - Máximo 999,999.99
     */
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio mínimo es 0.01")
    @DecimalMax(value = "999999.99", message = "El precio máximo es 999,999.99")
    @Digits(integer = 6, fraction = 2, message = "El precio debe tener máximo 6 dígitos enteros y 2 decimales")
    private BigDecimal price;

    /**
     * Número de entradas disponibles.
     * Validaciones:
     * - No puede ser nulo
     * - Debe ser un número positivo
     * - Mínimo 1 entrada
     * - Máximo 500,000 entradas
     */
    @NotNull(message = "El número de entradas disponibles es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 entrada disponible")
    @Max(value = 500000, message = "El máximo de entradas es 500,000")
    private Integer availableTickets;

    /**
     * URL de la imagen del evento.
     * Validaciones:
     * - Campo opcional
     * - Si se proporciona, debe ser una URL válida
     * - Máximo 500 caracteres
     */
    @Pattern(
        regexp = "^(https?://).*$", 
        message = "La URL debe comenzar con http:// o https://"
    )
    @Size(max = 500, message = "La URL no puede exceder 500 caracteres")
    private String imageUrl;

    /**
     * ID del lugar donde se realizará el evento.
     * Validaciones:
     * - Campo opcional (puede ser null)
     */
    private Long venueId;

    /**
     * Estado del evento (ACTIVE, CANCELLED, COMPLETED, POSTPONED).
     * Validaciones:
     * - Campo opcional en creación (por defecto será ACTIVE)
     * - Debe ser uno de los valores permitidos
     */
    @Pattern(
        regexp = "ACTIVE|CANCELLED|COMPLETED|POSTPONED",
        message = "El estado debe ser: ACTIVE, CANCELLED, COMPLETED o POSTPONED"
    )
    private String status;
}