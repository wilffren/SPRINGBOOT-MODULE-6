
package com.eventcatalog.exception;

/**
 * Excepción lanzada cuando se intenta crear un evento con un nombre que ya existe.
 * 
 * TASK 2: Manejo de validaciones - Duplicados en nombres de eventos
 * 
 * @author Event Catalog Team
 */
public class DuplicateEventException extends RuntimeException {
    
    public DuplicateEventException(String message) {
        super(message);
    }
    
    public DuplicateEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
