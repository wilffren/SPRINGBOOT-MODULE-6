package com.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuestas de error estandarizadas.
 * 
 * Proporciona una estructura consistente para todos los errores
 * que devuelve la API, facilitando el manejo en el frontend.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {
    
    /**
     * Timestamp del momento en que ocurrió el error.
     */
    private LocalDateTime timestamp;
    
    /**
     * Código de estado HTTP del error.
     */
    private Integer status;
    
    /**
     * Nombre del código de estado HTTP.
     */
    private String error;
    
    /**
     * Mensaje descriptivo del error.
     */
    private String message;
    
    /**
     * Path del endpoint donde ocurrió el error.
     */
    private String path;
    
    /**
     * Lista de errores de validación (si aplica).
     * Útil para errores de validación de múltiples campos.
     */
    private List<String> validationErrors;
}