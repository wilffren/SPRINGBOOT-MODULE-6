package com.example.HU4.infrastructure.controllers;

import com.example.HU4.application.services.EventService;
import com.example.HU4.infrastructure.dto.EventFilterRequest;
import com.example.HU4.infrastructure.dto.EventRequest;
import com.example.HU4.infrastructure.dto.EventResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Event
 * Endpoints para gestión de eventos
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * POST /api/events - Crear un nuevo evento
     */
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        EventResponse response = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/events/{id} - Obtener evento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        EventResponse response = eventService.getEventById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/events - Listar todos los eventos o buscar con filtros
     */
    @GetMapping
    public ResponseEntity<List<EventResponse>> searchEvents(
            @RequestParam(required = false) Long venueId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String nameContains) {

        // Si no hay filtros, devolver todos
        if (venueId == null && status == null && categoryName == null && nameContains == null) {
            return ResponseEntity.ok(eventService.getAllEvents());
        }

        // Aplicar filtros
        EventFilterRequest filter = new EventFilterRequest();
        filter.setVenueId(venueId);
        if (status != null) {
            filter.setStatus(com.example.HU4.domain.model.EventStatus.valueOf(status));
        }
        filter.setCategoryName(categoryName);
        filter.setNameContains(nameContains);

        List<EventResponse> results = eventService.searchEvents(filter);
        return ResponseEntity.ok(results);
    }

    /**
     * PUT /api/events/{id} - Actualizar un evento
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request) {
        EventResponse response = eventService.updateEvent(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/events/{id} - Eliminar un evento
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
