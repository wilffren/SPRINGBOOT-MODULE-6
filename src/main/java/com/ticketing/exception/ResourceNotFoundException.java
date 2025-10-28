package com.ticketing.exception;

/**
 * Excepción lanzada cuando un recurso no es encontrado en el sistema.
 * 
 * Esta excepción se utiliza en la capa de servicio cuando se intenta
 * acceder a un recurso (evento o venue) que no existe.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructor con mensaje personalizado.
     * 
     * @param message El mensaje descriptivo del error
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructor con mensaje y causa.
     * 
     * @param message El mensaje descriptivo del error
     * @param cause La causa raíz del error
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}