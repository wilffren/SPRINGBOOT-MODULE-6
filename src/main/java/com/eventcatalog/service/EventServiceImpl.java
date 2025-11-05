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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements IEventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    @Override
    @Transactional
    public EventResponseDTO create(EventRequestDTO eventRequest) {
        log.info("Intentando crear evento con nombre: {}", eventRequest.getName());

        if (eventRepository.existsByName(eventRequest.getName())) {
            throw new DuplicateEventException("Ya existe un evento con el nombre: " + eventRequest.getName());
        }

        VenueEntity venue = venueRepository.findById(eventRequest.getVenueId())
                .orElseThrow(() -> new VenueNotFoundException("No se encontró el lugar con ID: " + eventRequest.getVenueId()));

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

        EventEntity savedEvent = eventRepository.save(eventEntity);
        return mapToResponseDTO(savedEvent);
    }

    @Override
    public EventResponseDTO findById(Long id) {
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        return mapToResponseDTO(event);
    }

    @Override
    public List<EventResponseDTO> findAll() {
        return eventRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponseDTO<EventResponseDTO> findAllPaginated(Pageable pageable) {
        log.debug("Obteniendo eventos paginados");
        Page<EventEntity> page = eventRepository.findAll(pageable);
        Page<EventResponseDTO> dtoPage = page.map(this::mapToResponseDTO);
        return PageResponseDTO.fromPage(dtoPage);
    }

    @Override
    public PageResponseDTO<EventResponseDTO> findWithFilters(EventFilterDTO filters, Pageable pageable) {
        log.debug("Buscando eventos con filtros: {}", filters);

        // Validar nulos
        String city = filters != null ? filters.getCity() : null;
        String category = filters != null ? filters.getCategory() : null;
        LocalDateTime startDate = null;

        // Algunos DTOs lo llaman fechaInicio, otros startDate
        try {
            if (filters != null) {
                if (filters.getStartDate() != null) startDate = filters.getStartDate();
                else if (filters.getStartDate() != null) startDate = filters.getStartDate();
            }
        } catch (NoSuchMethodError ignored) {
            // Ignorar si el método no existe
        }

        Page<EventEntity> page = eventRepository.findByFilters(city, category, startDate, pageable);
        Page<EventResponseDTO> dtoPage = page.map(this::mapToResponseDTO);
        return PageResponseDTO.fromPage(dtoPage);
    }

    @Override
    public List<EventResponseDTO> findByCity(String city) {
        return eventRepository.findByCity(city)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponseDTO> findByCategory(String category) {
        return eventRepository.findByCategory(category)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponseDTO> findUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findByEventDateAfter(now)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventResponseDTO update(Long id, EventRequestDTO eventRequest) {
        EventEntity existing = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        if (eventRepository.existsByNameAndIdNot(eventRequest.getName(), id)) {
            throw new DuplicateEventException("Ya existe otro evento con el nombre: " + eventRequest.getName());
        }

        VenueEntity venue = venueRepository.findById(eventRequest.getVenueId())
                .orElseThrow(() -> new VenueNotFoundException("No se encontró el lugar con ID: " + eventRequest.getVenueId()));

        existing.setName(eventRequest.getName());
        existing.setDescription(eventRequest.getDescription());
        existing.setCategory(eventRequest.getCategory());
        existing.setCity(eventRequest.getCity());
        existing.setEventDate(eventRequest.getEventDate());
        existing.setPrice(eventRequest.getPrice());
        existing.setAvailableTickets(eventRequest.getAvailableTickets());
        existing.setImageUrl(eventRequest.getImageUrl());
        existing.setVenue(venue);

        if (eventRequest.getStatus() != null) {
            existing.setStatus(EventEntity.EventStatus.valueOf(eventRequest.getStatus()));
        }

        EventEntity updated = eventRepository.save(existing);
        return mapToResponseDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EventNotFoundException(id);
        }
        eventRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return eventRepository.existsByName(name);
    }

    private EventResponseDTO mapToResponseDTO(EventEntity event) {
        EventResponseDTO.VenueSummaryDTO venueSummary = EventResponseDTO.VenueSummaryDTO.builder()
                .id(event.getVenue().getId())
                .name(event.getVenue().getName())
                .address(event.getVenue().getAddress())
                .city(event.getVenue().getCity())
                .capacity(event.getVenue().getCapacity())
                .build();

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
