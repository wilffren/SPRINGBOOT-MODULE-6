package com.eventcatalog.service;

import com.eventcatalog.dto.EventFilterDTO;
import com.eventcatalog.dto.EventRequestDTO;
import com.eventcatalog.dto.EventResponseDTO;
import com.eventcatalog.dto.PageResponseDTO;
import com.eventcatalog.entity.EventEntity;
import com.eventcatalog.entity.VenueEntity;
import com.eventcatalog.exception.DuplicateEventException;
import com.eventcatalog.exception.EventNotFoundException;
import com.eventcatalog.exception.VenueNotFoundException;
import com.eventcatalog.repository.EventRepository;
import com.eventcatalog.repository.VenueRepository;
import com.eventcatalog.specification.EventSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de gestión de eventos.
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Solo maneja la lógica de negocio de eventos
 * - OCP (Open/Closed): Abierto para extensión mediante interfaces
 * - LSP (Liskov Substitution): Puede sustituir a IEventService
 * - ISP (Interface Segregation): Implementa solo la interfaz necesaria
 * - DIP (Dependency Inversion): Depende de abstracciones (Repositories)
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements IEventService {

    // Inyección de dependencias por constructor (inmutable)
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    /**
     * Crea un nuevo evento validando:
     * - Nombre no duplicado
     * - Venue existe (si se proporciona)
     * - Otras reglas de negocio
     * 
     * TASK 2: Validación de duplicados en nombres de eventos
     * 
     * @param eventRequest DTO con datos del evento
     * @return DTO con el evento creado
     */
    @Override
    @Transactional
    public EventResponseDTO create(EventRequestDTO eventRequest) {
        log.info("Intentando crear evento con nombre: {}", eventRequest.getName());
        
        // VALIDACIÓN 1: Verificar que no exista un evento con el mismo nombre
        if (eventRepository.existsByName(eventRequest.getName())) {
            log.warn("Intento de crear evento duplicado: {}", eventRequest.getName());
            throw new DuplicateEventException(
                "Ya existe un evento con el nombre: " + eventRequest.getName()
            );
        }
        
        // VALIDACIÓN 2: Verificar que el venue existe (si se proporciona)
        VenueEntity venue = null;
        if (eventRequest.getVenueId() != null) {
            venue = venueRepository.findById(eventRequest.getVenueId())
                    .orElseThrow(() -> new VenueNotFoundException(
                        "No se encontró el lugar con ID: " + eventRequest.getVenueId()
                    ));
        }
        
        // Convertir DTO a Entidad
        EventEntity eventEntity = EventEntity.builder()
                .name(eventRequest.getName())
                .description(eventRequest.getDescription())
                .category(eventRequest.getCategory())
                .city(eventRequest.getCity())
                .eventDate(eventRequest.getEventDate())
                .price(eventRequest.getPrice())
                .availableTickets(eventRequest.getAvailableTickets())
                .imageUrl(eventRequest.getImageUrl())
                .venue(venue)
                .status(eventRequest.getStatus() != null ? 
                    EventEntity.EventStatus.valueOf(eventRequest.getStatus()) :
                    EventEntity.EventStatus.ACTIVE)
                .build();
        
        // Guardar en la base de datos
        EventEntity savedEvent = eventRepository.save(eventEntity);
        log.info("Evento creado exitosamente con ID: {}", savedEvent.getId());
        
        // Convertir Entidad a DTO de respuesta
        return mapToResponseDTO(savedEvent);
    }

    /**
     * Busca un evento por su ID.
     * 
     * @param id ID del evento
     * @return DTO con información del evento
     */
    @Override
    public EventResponseDTO findById(Long id) {
        log.debug("Buscando evento con ID: {}", id);
        
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        
        return mapToResponseDTO(event);
    }

    /**
     * Obtiene todos los eventos.
     * 
     * @return Lista de eventos
     */
    @Override
    public List<EventResponseDTO> findAll() {
        log.debug("Obteniendo todos los eventos");
        
        return eventRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene eventos con paginación y ordenamiento.
     * 
     * TASK 3: Implementa paginación con Page y Pageable.
     * 
     * Ejemplos de uso:
     * - GET /events?page=0&size=10
     * - GET /events?page=0&size=10&sort=eventDate,asc
     * - GET /events?page=1&size=20&sort=name,desc
     * 
     * @param pageable Configuración de paginación
     * @return Página de eventos
     */
    @Override
    public PageResponseDTO<EventResponseDTO> findAllPaginated(Pageable pageable) {
        log.debug("Obteniendo eventos paginados: page={}, size={}", 
            pageable.getPageNumber(), pageable.getPageSize());
        
        // Obtener página de entidades
        Page<EventEntity> eventPage = eventRepository.findAll(pageable);
        
        // Convertir entidades a DTOs
        Page<EventResponseDTO> dtoPage = eventPage.map(this::mapToResponseDTO);
        
        // Crear respuesta con metadatos de paginación
        return PageResponseDTO.fromPage(dtoPage);
    }

    /**
     * Busca eventos aplicando filtros opcionales con paginación.
     * 
     * TASK 3: Filtros opcionales por ciudad, categoría, fechaInicio.
     * 
     * Ejemplos de uso:
     * - GET /events?page=0&size=10&city=Bogotá
     * - GET /events?page=0&size=10&category=Concierto&sort=eventDate,asc
     * - GET /events?page=0&size=10&city=Bogotá&category=Concierto&startDate=2025-12-01T00:00:00
     * 
     * @param filters DTO con filtros opcionales
     * @param pageable Configuración de paginación
     * @return Página de eventos filtrados
     */
    @Override
    public PageResponseDTO<EventResponseDTO> findWithFilters(
            EventFilterDTO filters, 
            Pageable pageable) {
        
        log.debug("Buscando eventos con filtros: {}, page={}, size={}", 
            filters, pageable.getPageNumber(), pageable.getPageSize());
        
        // Crear specification dinámica basada en los filtros
        Specification<EventEntity> spec = EventSpecification.withFilters(filters);
        
        // Ejecutar consulta con specification y paginación
        Page<EventEntity> eventPage = eventRepository.findAll(spec, pageable);
        
        log.debug("Eventos encontrados: {} de {} totales", 
            eventPage.getNumberOfElements(), eventPage.getTotalElements());
        
        // Convertir a DTOs
        Page<EventResponseDTO> dtoPage = eventPage.map(this::mapToResponseDTO);
        
        return PageResponseDTO.fromPage(dtoPage);
    }

    /**
     * Busca eventos por ciudad.
     * 
     * @param city Ciudad a buscar
     * @return Lista de eventos en esa ciudad
     */
    @Override
    public List<EventResponseDTO> findByCity(String city) {
        log.debug("Buscando eventos en ciudad: {}", city);
        
        return eventRepository.findByCity(city)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca eventos por categoría.
     * 
     * @param category Categoría a buscar
     * @return Lista de eventos de esa categoría
     */
    @Override
    public List<EventResponseDTO> findByCategory(String category) {
        log.debug("Buscando eventos de categoría: {}", category);
        
        return eventRepository.findByCategory(category)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene eventos futuros (después de la fecha actual).
     * 
     * @return Lista de eventos futuros
     */
    @Override
    public List<EventResponseDTO> findUpcomingEvents() {
        log.debug("Obteniendo eventos futuros");
        
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findByEventDateAfter(now)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza un evento existente.
     * 
     * TASK 2: Validar duplicados excluyendo el propio registro
     * 
     * @param id ID del evento a actualizar
     * @param eventRequest DTO con nuevos datos
     * @return DTO con evento actualizado
     */
    @Override
    @Transactional
    public EventResponseDTO update(Long id, EventRequestDTO eventRequest) {
        log.info("Actualizando evento con ID: {}", id);
        
        // Verificar que el evento existe
        EventEntity existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        
        // Validar que el nuevo nombre no esté duplicado (excluyendo este registro)
        if (eventRepository.existsByNameAndIdNot(eventRequest.getName(), id)) {
            log.warn("Intento de actualizar a nombre duplicado: {}", eventRequest.getName());
            throw new DuplicateEventException(
                "Ya existe otro evento con el nombre: " + eventRequest.getName()
            );
        }
        
        // Verificar que el venue existe (si se proporciona)
        VenueEntity venue = null;
        if (eventRequest.getVenueId() != null) {
            venue = venueRepository.findById(eventRequest.getVenueId())
                    .orElseThrow(() -> new VenueNotFoundException(
                        "No se encontró el lugar con ID: " + eventRequest.getVenueId()
                    ));
        }
        
        // Actualizar campos
        existingEvent.setName(eventRequest.getName());
        existingEvent.setDescription(eventRequest.getDescription());
        existingEvent.setCategory(eventRequest.getCategory());
        existingEvent.setCity(eventRequest.getCity());
        existingEvent.setEventDate(eventRequest.getEventDate());
        existingEvent.setPrice(eventRequest.getPrice());
        existingEvent.setAvailableTickets(eventRequest.getAvailableTickets());
        existingEvent.setImageUrl(eventRequest.getImageUrl());
        existingEvent.setVenue(venue);
        
        if (eventRequest.getStatus() != null) {
            existingEvent.setStatus(EventEntity.EventStatus.valueOf(eventRequest.getStatus()));
        }
        
        // Guardar cambios
        EventEntity updatedEvent = eventRepository.save(existingEvent);
        log.info("Evento actualizado exitosamente con ID: {}", updatedEvent.getId());
        
        return mapToResponseDTO(updatedEvent);
    }

    /**
     * Elimina un evento por su ID.
     * 
     * @param id ID del evento a eliminar
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Eliminando evento con ID: {}", id);
        
        // Verificar que el evento existe
        if (!eventRepository.existsById(id)) {
            throw new EventNotFoundException(id);
        }
        
        eventRepository.deleteById(id);
        log.info("Evento eliminado exitosamente con ID: {}", id);
    }

    /**
     * Verifica si existe un evento con el nombre especificado.
     * 
     * @param name Nombre del evento
     * @return true si existe
     */
    @Override
    public boolean existsByName(String name) {
        return eventRepository.existsByName(name);
    }

    /**
     * Método helper para convertir Entidad a DTO de respuesta.
     * Aplica el principio DRY (Don't Repeat Yourself).
     * 
     * @param event Entidad EventEntity
     * @return DTO EventResponseDTO
     */
    private EventResponseDTO mapToResponseDTO(EventEntity event) {
        // Mapear información del venue de forma resumida (si existe)
        EventResponseDTO.VenueSummaryDTO venueSummary = null;
        if (event.getVenue() != null) {
            venueSummary = EventResponseDTO.VenueSummaryDTO.builder()
                    .id(event.getVenue().getId())
                    .name(event.getVenue().getName())
                    .address(event.getVenue().getAddress())
                    .city(event.getVenue().getCity())
                    .capacity(event.getVenue().getCapacity())
                    .build();
        }
        
        return EventResponseDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .category(event.getCategory())
                .city(event.getCity())
                .eventDate(event.getEventDate())
                .price(event.getPrice())
                .availableTickets(event.getAvailableTickets())
                .imageUrl(event.getImageUrl())
                .status(event.getStatus().name())
                .venue(venueSummary)
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}