package com.eventcatalog.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Clase para estructurar las respuestas de error de forma consistente.
 * 
 * TASK 2: Implementar mensajes de error descriptivos en el payload
 * 
 * @author Event Catalog Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    
    /**
     * Timestamp del error
     */
    private LocalDateTime timestamp;
    
    /**
     * Código de estado HTTP
     */
    private int status;
    
    /**
     * Nombre del error HTTP (ej: "Bad Request", "Not Found")
     */
    private String error;
    
    /**
     * Mensaje principal del error
     */
    private String message;
    
    /**
     * Ruta donde ocurrió el error
     */
    private String path;
    
    /**
     * Lista de errores de validación (para múltiples campos)
     * Útil cuando hay múltiples errores de validación
     */
    private List<ValidationError> validationErrors;
    
    /**
     * Detalles adicionales del error (opcional)
     */
    private Map<String, Object> details;
    
    /**
     * Clase interna para representar errores de validación individuales
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ValidationError {
        /**
         * Campo que tiene el error
         */
        private String field;
        
        /**
         * Mensaje de error específico del campo
         */
        private String message;
        
        /**
         * Valor rechazado (opcional)
         */
        private Object rejectedValue;
    }
}