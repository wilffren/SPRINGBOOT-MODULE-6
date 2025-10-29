package com.eventcatalog.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para recibir datos de creación/actualización de lugares.
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Solo maneja la transferencia de datos de entrada
 * - ISP (Interface Segregation): No hereda métodos innecesarios
 * 
 * TASK 2: Implementa validaciones con Bean Validation (jakarta.validation)
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueRequestDTO {

    /**
     * Nombre del lugar.
     * Validaciones:
     * - No puede ser nulo o vacío
     * - Debe tener entre 3 y 100 caracteres
     * - No puede contener solo espacios en blanco
     */
    @NotBlank(message = "El nombre del lugar es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    /**
     * Dirección completa del lugar.
     * Validaciones:
     * - No puede ser nula o vacía
     * - Debe tener entre 10 y 200 caracteres
     */
    @NotBlank(message = "La dirección es obligatoria")
    @Size(min = 10, max = 200, message = "La dirección debe tener entre 10 y 200 caracteres")
    private String address;

    /**
     * Ciudad donde se encuentra el lugar.
     * Validaciones:
     * - No puede ser nula o vacía
     * - Debe tener entre 2 y 50 caracteres
     */
    @NotBlank(message = "La ciudad es obligatoria")
    @Size(min = 2, max = 50, message = "La ciudad debe tener entre 2 y 50 caracteres")
    private String city;

    /**
     * Capacidad máxima del lugar.
     * Validaciones:
     * - No puede ser nulo
     * - Debe ser un número positivo
     * - Mínimo 10 personas
     * - Máximo 500,000 personas (para estadios grandes)
     */
    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 10, message = "La capacidad mínima es de 10 personas")
    @Max(value = 500000, message = "La capacidad máxima es de 500,000 personas")
    private Integer capacity;

    /**
     * Descripción adicional del lugar.
     * Validaciones:
     * - Campo opcional
     * - Si se proporciona, máximo 500 caracteres
     */
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String description;
}