package com.example.HU4.application.services;

import com.example.HU4.infrastructure.dto.*;
import com.example.HU4.infrastructure.entities.EventEntity;
import com.example.HU4.infrastructure.entities.VenueEntity;
import com.example.HU4.infrastructure.entities.CategoryEntity;
import com.example.HU4.infrastructure.repositories.*;
import com.example.HU4.infrastructure.repositories.specifications.EventSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de eventos
 * Aplica @Transactional apropiadamente según el tipo de operación
 */
@Service
public class EventService {

    private final JpaEventRepository eventRepository;
    private final JpaVenueRepository venueRepository;
    private final JpaCategoryRepository categoryRepository;

    public EventService(JpaEventRepository eventRepository,
            JpaVenueRepository venueRepository,
            JpaCategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Crear un nuevo evento
     * 
     * @Transactional sin readOnly para operación de escritura
     */
    @Transactional
    public EventResponse createEvent(EventRequest request) {
        VenueEntity venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue no encontrado con ID: " + request.getVenueId()));

        EventEntity event = new EventEntity();
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setStatus(
                request.getStatus() != null ? request.getStatus() : com.example.HU4.domain.model.EventStatus.ACTIVE);
        event.setVenue(venue);

        // Asignar categorías si existen
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<CategoryEntity> categories = categoryRepository.findAllById(request.getCategoryIds());
            event.setCategories(categories);
        }

        EventEntity saved = eventRepository.save(event);
        return toResponse(saved);
    }

    /**
     * Obtener evento por ID
     * 
     * @Transactional(readOnly = true) para operación de lectura
     */
    @Transactional(readOnly = true)
    public EventResponse getEventById(Long id) {
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));
        return toResponse(event);
    }

    /**
     * Listar todos los eventos
     * 
     * @Transactional(readOnly = true) para operación de lectura
     */
    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Buscar eventos con filtros dinámicos usando Specifications
     * 
     * @Transactional(readOnly = true) para operación de lectura
     */
    @Transactional(readOnly = true)
    public List<EventResponse> searchEvents(EventFilterRequest filter) {
        Specification<EventEntity> spec = Specification.where(null);

        if (filter.getVenueId() != null) {
            spec = spec.and(EventSpecifications.hasVenue(filter.getVenueId()));
        }
        if (filter.getStatus() != null) {
            spec = spec.and(EventSpecifications.hasStatus(filter.getStatus()));
        }
        if (filter.getStartDateFrom() != null) {
            spec = spec.and(EventSpecifications.startDateAfter(filter.getStartDateFrom()));
        }
        if (filter.getEndDateTo() != null) {
            spec = spec.and(EventSpecifications.endDateBefore(filter.getEndDateTo()));
        }
        if (filter.getCategoryName() != null) {
            spec = spec.and(EventSpecifications.hasCategory(filter.getCategoryName()));
        }
        if (filter.getNameContains() != null) {
            spec = spec.and(EventSpecifications.nameContains(filter.getNameContains()));
        }

        return eventRepository.findAll(spec).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener eventos de un venue específico
     * 
     * @Transactional(readOnly = true) para operación de lectura
     */
    @Transactional(readOnly = true)
    public List<EventResponse> getEventsByVenue(Long venueId) {
        return eventRepository.findByVenueId(venueId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar un evento
     * 
     * @Transactional sin readOnly para operación de escritura
     */
    @Transactional
    public EventResponse updateEvent(Long id, EventRequest request) {
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));

        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        if (request.getStatus() != null) {
            event.setStatus(request.getStatus());
        }

        if (request.getVenueId() != null && !request.getVenueId().equals(event.getVenue().getId())) {
            VenueEntity newVenue = venueRepository.findById(request.getVenueId())
                    .orElseThrow(() -> new RuntimeException("Venue no encontrado"));
            event.setVenue(newVenue);
        }

        EventEntity updated = eventRepository.save(event);
        return toResponse(updated);
    }

    /**
     * Eliminar un evento
     * 
     * @Transactional sin readOnly para operación de escritura
     */
    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Evento no encontrado con ID: " + id);
        }
        eventRepository.deleteById(id);
    }

    // Método auxiliar para convertir Entity a Response DTO
    private EventResponse toResponse(EventEntity entity) {
        VenueResponse venueResponse = new VenueResponse(
                entity.getVenue().getId(),
                entity.getVenue().getName(),
                entity.getVenue().getLocation(),
                entity.getVenue().getCapacity(),
                entity.getVenue().getDescription(),
                entity.getVenue().getCreatedAt(),
                entity.getVenue().getUpdatedAt());

        List<String> categoryNames = entity.getCategories().stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toList());

        return new EventResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStatus(),
                venueResponse,
                categoryNames,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
