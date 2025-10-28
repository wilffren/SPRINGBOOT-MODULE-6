package com.ticketing.repository;

import com.ticketing.dto.VenueDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación In-Memory del repositorio de venues.
 * 
 * Almacena venues en memoria usando un ConcurrentHashMap.
 * Aplica los principios S y L de SOLID.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
@Repository
public class VenueRepositoryImpl implements IVenueRepository {
    
    // ConcurrentHashMap para almacenamiento thread-safe en memoria
    private final ConcurrentHashMap<Long, VenueDTO> venueStore = new ConcurrentHashMap<>();
    
    // Generador de IDs autoincremental
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public VenueDTO save(VenueDTO venue) {
        // Asignar ID si no existe
        if (venue.getId() == null) {
            venue.setId(idGenerator.getAndIncrement());
        }
        
        // Guardar en el store
        venueStore.put(venue.getId(), venue);
        
        return venue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<VenueDTO> findById(Long id) {
        return Optional.ofNullable(venueStore.get(id));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<VenueDTO> findAll() {
        return new ArrayList<>(venueStore.values());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<VenueDTO> update(Long id, VenueDTO venue) {
        if (!venueStore.containsKey(id)) {
            return Optional.empty();
        }
        
        // Mantener el ID original
        venue.setId(id);
        
        // Actualizar en el store
        venueStore.put(id, venue);
        
        return Optional.of(venue);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteById(Long id) {
        return venueStore.remove(id) != null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(Long id) {
        return venueStore.containsKey(id);
    }
}