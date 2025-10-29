package com.eventcatalog.exception;

/**
 * Excepción lanzada cuando se intenta crear un lugar con un nombre que ya existe.
 * 
 * @author Event Catalog Team
 */
public class DuplicateVenueException extends RuntimeException {
    
    public DuplicateVenueException(String message) {
        super(message);
    }
    
    public DuplicateVenueException(String message, Throwable cause) {
        super(message, cause);
    }
}