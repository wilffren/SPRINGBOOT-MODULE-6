package com.ticketing.service;

import com.ticketing.dto.VenueDTO;

import java.util.List;

/**
 * Interface del servicio de venues.
 * 
 * Define el contrato para la lógica de negocio relacionada con venues.
 * Aplica los principios O y D de SOLID.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
public interface IVenueService {
    
    /**
     * Crea un nuevo venue en el sistema.
     * 
     * @param venue Los datos del venue a crear
     * @return El venue creado con su ID asignado
     */
    VenueDTO createVenue(VenueDTO venue);
    
    /**
     * Obtiene un venue por su ID.
     * 
     * @param id El ID del venue a buscar
     * @return El venue encontrado
     * @throws com.ticketing.exception.ResourceNotFoundException si el venue no existe
     */
    VenueDTO getVenueById(Long id);
    
    /**
     * Obtiene todos los venues del sistema.
     * 
     * @return Lista de todos los venues
     */
    List<VenueDTO> getAllVenues();
    
    /**
     * Actualiza un venue existente.
     * 
     * @param id El ID del venue a actualizar
     * @param venue Los nuevos datos del venue
     * @return El venue actualizado
     * @throws com.ticketing.exception.ResourceNotFoundException si el venue no existe
     */
    VenueDTO updateVenue(Long id, VenueDTO venue);
    
    /**
     * Elimina un venue del sistema.
     * 
     * @param id El ID del venue a eliminar
     * @throws com.ticketing.exception.ResourceNotFoundException si el venue no existe
     */
    void deleteVenue(Long id);
}