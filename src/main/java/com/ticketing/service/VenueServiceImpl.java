package com.ticketing.service;

import com.ticketing.dto.VenueDTO;
import com.ticketing.exception.ResourceNotFoundException;
import com.ticketing.repository.IVenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio de venues.
 * 
 * Contiene la lógica de negocio para la gestión de venues.
 * Aplica los principios S y D de SOLID.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VenueServiceImpl implements IVenueService {
    
    // Inyección de dependencias por constructor
    private final IVenueRepository venueRepository;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public VenueDTO createVenue(VenueDTO venue) {
        log.info("Creando nuevo venue: {}", venue.getName());
        
        // Establecer valores por defecto
        if (venue.getActive() == null) {
            venue.setActive(true);
        }
        
        VenueDTO savedVenue = venueRepository.save(venue);
        
        log.info("Venue creado exitosamente con ID: {}", savedVenue.getId());
        return savedVenue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public VenueDTO getVenueById(Long id) {
        log.info("Buscando venue con ID: {}", id);
        
        return venueRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Venue no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Venue no encontrado con ID: " + id);
                });
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<VenueDTO> getAllVenues() {
        log.info("Obteniendo todos los venues");
        
        List<VenueDTO> venues = venueRepository.findAll();
        
        log.info("Se encontraron {} venues", venues.size());
        return venues;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public VenueDTO updateVenue(Long id, VenueDTO venue) {
        log.info("Actualizando venue con ID: {}", id);
        
        // Verificar que el venue existe
        if (!venueRepository.existsById(id)) {
            log.error("Venue no encontrado con ID: {}", id);
            throw new ResourceNotFoundException("Venue no encontrado con ID: " + id);
        }
        
        VenueDTO updatedVenue = venueRepository.update(id, venue)
                .orElseThrow(() -> new ResourceNotFoundException("Error al actualizar venue con ID: " + id));
        
        log.info("Venue actualizado exitosamente con ID: {}", id);
        return updatedVenue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteVenue(Long id) {
        log.info("Eliminando venue con ID: {}", id);
        
        // Verificar que el venue existe
        if (!venueRepository.existsById(id)) {
            log.error("Venue no encontrado con ID: {}", id);
            throw new ResourceNotFoundException("Venue no encontrado con ID: " + id);
        }
        
        venueRepository.deleteById(id);
        
        log.info("Venue eliminado exitosamente con ID: {}", id);
    }
}