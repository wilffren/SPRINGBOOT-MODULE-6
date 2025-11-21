package com.example.HU4.application.services;

import com.example.HU4.infrastructure.dto.VenueRequest;
import com.example.HU4.infrastructure.dto.VenueResponse;
import com.example.HU4.infrastructure.entities.VenueEntity;
import com.example.HU4.infrastructure.repositories.JpaVenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de venues
 * Aplica @Transactional apropiadamente según el tipo de operación
 */
@Service
public class VenueService {

    private final JpaVenueRepository venueRepository;

    public VenueService(JpaVenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    /**
     * Crear un nuevo venue
     * 
     * @Transactional sin readOnly para operación de escritura
     */
    @Transactional
    public VenueResponse createVenue(VenueRequest request) {
        VenueEntity venue = new VenueEntity();
        venue.setName(request.getName());
        venue.setLocation(request.getLocation());
        venue.setCapacity(request.getCapacity());
        venue.setDescription(request.getDescription());

        VenueEntity saved = venueRepository.save(venue);
        return toResponse(saved);
    }

    /**
     * Obtener venue por ID
     * 
     * @Transactional(readOnly = true) para operación de lectura
     */
    @Transactional(readOnly = true)
    public VenueResponse getVenueById(Long id) {
        VenueEntity venue = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue no encontrado con ID: " + id));
        return toResponse(venue);
    }

    /**
     * Listar todos los venues
     * 
     * @Transactional(readOnly = true) para operación de lectura
     */
    @Transactional(readOnly = true)
    public List<VenueResponse> getAllVenues() {
        return venueRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Buscar venues por capacidad mínima
     * 
     * @Transactional(readOnly = true) para operación de lectura
     */
    @Transactional(readOnly = true)
    public List<VenueResponse> getVenuesByMinCapacity(Integer minCapacity) {
        return venueRepository.findByCapacityGreaterThanEqual(minCapacity).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Buscar venues por ubicación
     * 
     * @Transactional(readOnly = true) para operación de lectura
     */
    @Transactional(readOnly = true)
    public List<VenueResponse> getVenuesByLocation(String location) {
        return venueRepository.findByLocationContainingIgnoreCase(location).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar un venue
     * 
     * @Transactional sin readOnly para operación de escritura
     */
    @Transactional
    public VenueResponse updateVenue(Long id, VenueRequest request) {
        VenueEntity venue = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue no encontrado con ID: " + id));

        venue.setName(request.getName());
        venue.setLocation(request.getLocation());
        venue.setCapacity(request.getCapacity());
        venue.setDescription(request.getDescription());

        VenueEntity updated = venueRepository.save(venue);
        return toResponse(updated);
    }

    /**
     * Eliminar un venue
     * 
     * @Transactional sin readOnly para operación de escritura
     *                Nota: Los eventos relacionados se eliminarán en cascada
     */
    @Transactional
    public void deleteVenue(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new RuntimeException("Venue no encontrado con ID: " + id);
        }
        venueRepository.deleteById(id);
    }

    // Método auxiliar para convertir Entity a Response DTO
    private VenueResponse toResponse(VenueEntity entity) {
        return new VenueResponse(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getCapacity(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
