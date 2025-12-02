package com.eventcatalog.exception;

/**
 * Excepción lanzada cuando se intenta eliminar un recurso que tiene dependencias.
 * 
 * @author Event Catalog Team
 */
public class ResourceInUseException extends RuntimeException {
    
    public ResourceInUseException(String message) {
        super(message);
    }
    
    public ResourceInUseException(String resourceType, Long id, String dependency) {
        super(String.format(
            "No se puede eliminar %s con ID %d porque tiene %s asociados",
            resourceType, id, dependency
        ));
    }
}