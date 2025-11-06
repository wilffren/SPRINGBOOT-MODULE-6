package com.eventcatalog.service;

import com.eventcatalog.dto.VenueRequestDTO;
import com.eventcatalog.dto.VenueResponseDTO;
import com.eventcatalog.entity.VenueEntity;
import com.eventcatalog.exception.DuplicateVenueException;
import com.eventcatalog.exception.VenueNotFoundException;
import com.eventcatalog.repository.VenueRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de la interfaz IVenueService para la gestión de lugares (Venues).
 *
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Gestiona la lógica de negocio relacionada con Venue.
 * - DIP (Dependency Inversion): Depende de la abstracción IVenueService y de VenueRepository.
 * - OCP (Open/Closed): Abierto a extensión mediante nuevas reglas o validaciones.
 *
 * @author Event Catalog Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements IVenueService {

    private final VenueRepository venueRepository;

    /**
     * Crea un nuevo lugar si no existe otro con el mismo nombre.
     */
    @Override
    @Transactional
    public VenueResponseDTO create(VenueRequestDTO venueRequest) {
        if (venueRepository.existsByName(venueRequest.getName())) {
            throw new DuplicateVenueException("A venue with name '" + venueRequest.getName() + "' already exists");
        }

        VenueEntity venue = VenueEntity.builder()
                .name(venueRequest.getName())
                .address(venueRequest.getAddress())
                .city(venueRequest.getCity())
                .capacity(venueRequest.getCapacity())
                .description(venueRequest.getDescription())
                .build();

        VenueEntity savedVenue = venueRepository.save(venue);
        return mapToResponseDTO(savedVenue);
    }

    /**
     * Obtiene un lugar por su ID.
     */
    @Override
    @Transactional
    public VenueResponseDTO findById(Long id) {
        VenueEntity venue = venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException("Venue with id " + id + " not found"));
        return mapToResponseDTO(venue);
    }

    /**
     * Obtiene todos los lugares registrados.
     */
    @Override
    @Transactional
    public List<VenueResponseDTO> findAll() {
        return venueRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca lugares por ciudad.
     */
    @Override
    @Transactional
    public List<VenueResponseDTO> findByCity(String city) {
        return venueRepository.findByCity(city)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza un lugar existente.
     */
    @Override
    @Transactional
    public VenueResponseDTO update(Long id, VenueRequestDTO venueRequest) {
        VenueEntity venue = venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException("Venue with id " + id + " not found"));

        // Validar nombre duplicado
        if (venueRepository.existsByNameAndIdNot(venueRequest.getName(), id)) {
            throw new DuplicateVenueException("A venue with name '" + venueRequest.getName() + "' already exists");
        }

        venue.setName(venueRequest.getName());
        venue.setAddress(venueRequest.getAddress());
        venue.setCity(venueRequest.getCity());
        venue.setCapacity(venueRequest.getCapacity());
        venue.setDescription(venueRequest.getDescription());

        VenueEntity updatedVenue = venueRepository.save(venue);
        return mapToResponseDTO(updatedVenue);
    }

    /**
     * Elimina un lugar por su ID.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        VenueEntity venue = venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException("Venue with id " + id + " not found"));
        venueRepository.delete(venue);
    }

    /**
     * Verifica si existe un lugar con el nombre especificado.
     */
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public boolean existsByName(String name) {
        return venueRepository.existsByName(name);
    }

    // ----------------------------------------------------
    // Mapper de entidad a DTO
    // ----------------------------------------------------
    private VenueResponseDTO mapToResponseDTO(VenueEntity venue) {
        Integer eventCount = venueRepository.countEventsByVenueId(venue.getId());

        return VenueResponseDTO.builder()
                .id(venue.getId())
                .name(venue.getName())
                .address(venue.getAddress())
                .city(venue.getCity())
                .capacity(venue.getCapacity())
                .description(venue.getDescription())
                .createdAt(venue.getCreatedAt())
                .updatedAt(venue.getUpdatedAt())
                .eventCount(eventCount)
                .build();
    }
}
