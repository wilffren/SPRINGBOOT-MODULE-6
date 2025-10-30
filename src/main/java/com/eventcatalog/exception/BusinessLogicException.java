package com.eventcatalog.exception;

/**
 * Excepción lanzada cuando se intenta realizar una operación no permitida.
 * 
 * @author Event Catalog Team
 */
public class BusinessLogicException extends RuntimeException {
    
    public BusinessLogicException(String message) {
        super(message);
    }
    
    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}