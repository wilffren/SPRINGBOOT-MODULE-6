
package com.eventcatalog.exception;

/**
 * Excepción lanzada cuando un parámetro de paginación es inválido.
 * 
 * TASK 4: Manejo básico de errores
 * 
 * @author Event Catalog Team
 */
public class InvalidPaginationException extends RuntimeException {
    
    public InvalidPaginationException(String message) {
        super(message);
    }
    
    public InvalidPaginationException(String parameter, String reason) {
        super(String.format("Parámetro de paginación inválido '%s': %s", parameter, reason));
    }
}