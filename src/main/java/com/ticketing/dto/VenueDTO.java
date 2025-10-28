package com.ticketing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferir datos de Venues (Lugares/Recintos) entre capas.
 * 
 * Representa un lugar físico donde se realizan eventos.
 * Incluye validaciones para garantizar la integridad de los datos.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueDTO {
    
    /**
     * Identificador único del venue.
     * Se genera automáticamente en el servicio.
     */
    private Long id;
    
    /**
     * Nombre del venue.
     * No puede estar vacío y debe tener entre 3 y 100 caracteres.
     */
    @NotBlank(message = "El nombre del venue es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;
    
    /**
     * Dirección física del venue.
     */
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String address;
    
    /**
     * Ciudad donde se encuentra el venue.
     */
    @NotBlank(message = "La ciudad es obligatoria")
    private String city;
    
    /**
     * País donde se encuentra el venue.
     */
    @NotBlank(message = "El país es obligatorio")
    private String country;
    
    /**
     * Capacidad máxima del venue.
     */
    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser mayor a 0")
    private Integer capacity;
    
    /**
     * Tipo de venue (estadio, teatro, arena, etc.).
     */
    @NotBlank(message = "El tipo de venue es obligatorio")
    private String type;
    
    /**
     * Información de contacto del venue.
     */
    private String contactInfo;
    
    /**
     * Indica si el venue está activo o no.
     */
    private Boolean active;
}