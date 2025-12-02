package com.eventcatalog.exception;

/**
 * Excepción lanzada cuando no se encuentra un evento por su ID.
 * 
 * @author Event Catalog Team
 */
public class EventNotFoundException extends RuntimeException {
    
    public EventNotFoundException(String message) {
        super(message);
    }
    
    public EventNotFoundException(Long id) {
        super("Evento no encontrado con ID: " + id);
    }
}