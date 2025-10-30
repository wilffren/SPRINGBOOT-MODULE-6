package com.eventcatalog.exception;

/**
 * Excepción lanzada cuando un filtro contiene valores inválidos.
 * 
 * @author Event Catalog Team
 */
public class InvalidFilterException extends RuntimeException {
    
    public InvalidFilterException(String message) {
        super(message);
    }
    
    public InvalidFilterException(String filterName, String reason) {
        super(String.format("Filtro inválido '%s': %s", filterName, reason));
    }
}