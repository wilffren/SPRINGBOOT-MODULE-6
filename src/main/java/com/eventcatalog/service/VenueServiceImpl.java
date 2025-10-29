package com.eventcatalog.service;

import com.eventcatalog.dto.VenueRequestDTO;
import com.eventcatalog.dto.VenueResponseDTO;
import com.eventcatalog.entity.VenueEntity;
import com.eventcatalog.exception.DuplicateVenueException;
import com.eventcatalog.exception.VenueNotFoundException;
import com.eventcatalog.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de gestión de lugares.
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Solo maneja la lógica de negocio de lugares
 * - OCP (Open/Closed): Abierto para extensión, cerrado para modificación
 * - LSP (Liskov Substitution): Puede sustituir a IVenueService
 * - ISP (Interface Segregation): Implementa solo la interfaz necesaria
 * - DIP (Dependency Inversion): Depende de abstracciones (VenueRepository)
 * 
 * @RequiredArgsConstructor: Genera constructor con las dependencias finales
 * @Transactional: Manejo transaccional de operaciones de escritura
 * @Slf4j: Logging con Lombok
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VenueServiceImpl implements IVenueService {

    // Inyección de dependencias por constructor (inmutable)
    private final VenueRepository venueRepository;

    /**
     * Crea un nuevo lugar validando que el nombre no esté duplicado.
     * 
     * TASK 2: Validar duplicados en nombres
     * 
     * @param venueRequest DTO con datos del lugar
     * @return DTO con el lugar creado
     */
    @Override
    @Transactional
    public VenueResponseDTO create(VenueRequestDTO venueRequest) {
        log.info("Intentando crear lugar con nombre: {}", venueRequest.getName());
        
        // Validar que no exista un lugar con el mismo nombre
        if (venueRepository.existsByName(venueRequest.getName())) {
            log.warn("Intento de crear lugar duplicado: {}", venueRequest.getName());
            throw new DuplicateVenueException(
                "Ya existe un lugar con el nombre: " + venueRequest.getName()
            );
        }
        
        // Convertir DTO a Entidad
        VenueEntity venueEntity = VenueEntity.builder()
                .name(venueRequest.getName())
                .address(venueRequest.getAddress())
                .city(venueRequest.getCity())
                .capacity(venueRequest.getCapacity())
                .description(venueRequest.getDescription())
                .build();
        
        // Guardar en la base de datos
        VenueEntity savedVenue = venueRepository.save(venueEntity);
        log.info("Lugar creado exitosamente con ID: {}", savedVenue.getId());
        
        // Convertir Entidad a DTO de respuesta
        return mapToResponseDTO(savedVenue);
    }

    /**
     * Busca un lugar por su ID.
     * 
     * @param id ID del lugar
     * @return DTO con información del lugar
     */
    @Override
    public VenueResponseDTO findById(Long id) {
        log.debug("Buscando lugar con ID: {}", id);
        
        VenueEntity venue = venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException(id));
        
        return mapToResponseDTO(venue);
    }

    /**
     * Obtiene todos los lugares.
     * 
     * @return Lista de lugares
     */
    @Override
    public List<VenueResponseDTO> findAll() {
        log.debug("Obteniendo todos los lugares");
        
        return venueRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca lugares por ciudad.
     * 
     * @param city Ciudad a buscar
     * @return Lista de lugares en esa ciudad
     */
    @Override
    public List<VenueResponseDTO> findByCity(String city) {
        log.debug("Buscando lugares en ciudad: {}", city);
        
        return venueRepository.findByCityIgnoreCase(city)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza un lugar existente.
     * 
     * TASK 2: Validar duplicados excluyendo el propio registro
     * 
     * @param id ID del lugar a actualizar
     * @param venueRequest DTO con nuevos datos
     * @return DTO con lugar actualizado
     */
    @Override
    @Transactional
    public VenueResponseDTO update(Long id, VenueRequestDTO venueRequest) {
        log.info("Actualizando lugar con ID: {}", id);
        
        // Verificar que el lugar existe
        VenueEntity existingVenue = venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException(id));
        
        // Validar que el nuevo nombre no esté duplicado (excluyendo este registro)
        if (venueRepository.existsByNameAndIdNot(venueRequest.getName(), id)) {
            log.warn("Intento de actualizar a nombre duplicado: {}", venueRequest.getName());
            throw new DuplicateVenueException(
                "Ya existe otro lugar con el nombre: " + venueRequest.getName()
            );
        }
        
        // Actualizar campos
        existingVenue.setName(venueRequest.getName());
        existingVenue.setAddress(venueRequest.getAddress());
        existingVenue.setCity(venueRequest.getCity());
        existingVenue.setCapacity(venueRequest.getCapacity());
        existingVenue.setDescription(venueRequest.getDescription());
        
        // Guardar cambios
        VenueEntity updatedVenue = venueRepository.save(existingVenue);
        log.info("Lugar actualizado exitosamente con ID: {}", updatedVenue.getId());
        
        return mapToResponseDTO(updatedVenue);
    }

    /**
     * Elimina un lugar por su ID.
     * 
     * @param id ID del lugar a eliminar
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Eliminando lugar con ID: {}", id);
        
        // Verificar que el lugar existe
        if (!venueRepository.existsById(id)) {
            throw new VenueNotFoundException(id);
        }
        
        venueRepository.deleteById(id);
        log.info("Lugar eliminado exitosamente con ID: {}", id);
    }

    /**
     * Verifica si existe un lugar con el nombre especificado.
     * 
     * @param name Nombre del lugar
     * @return true si existe
     */
    @Override
    public boolean existsByName(String name) {
        return venueRepository.existsByName(name);
    }

    /**
     * Método helper para convertir Entidad a DTO de respuesta.
     * Aplica el principio DRY (Don't Repeat Yourself).
     * 
     * @param venue Entidad VenueEntity
     * @return DTO VenueResponseDTO
     */
    private VenueResponseDTO mapToResponseDTO(VenueEntity venue) {
        return VenueResponseDTO.builder()
                .id(venue.getId())
                .name(venue.getName())
                .address(venue.getAddress())
                .city(venue.getCity())
                .capacity(venue.getCapacity())
                .description(venue.getDescription())
                .createdAt(venue.getCreatedAt())
                .updatedAt(venue.getUpdatedAt())
                .eventCount(venue.getEvents() != null ? venue.getEvents().size() : 0)
                .build();
    }
}