package com.eventcatalog.exception;

/**
 * Excepción lanzada cuando no se encuentra un lugar por su ID.
 * 
 * @author Event Catalog Team
 */
public class VenueNotFoundException extends RuntimeException {
    
    public VenueNotFoundException(String message) {
        super(message);
    }
    
    public VenueNotFoundException(Long id) {
        super("Lugar no encontrado con ID: " + id);
    }
}