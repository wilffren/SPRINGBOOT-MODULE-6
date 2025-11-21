package com.example.HU4.infrastructure.controllers;

import com.example.HU4.application.services.EventService;
import com.example.HU4.application.services.VenueService;
import com.example.HU4.infrastructure.dto.EventResponse;
import com.example.HU4.infrastructure.dto.VenueRequest;
import com.example.HU4.infrastructure.dto.VenueResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Venue
 * Endpoints para gestión de venues
 */
@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueService venueService;
    private final EventService eventService;

    public VenueController(VenueService venueService, EventService eventService) {
        this.venueService = venueService;
        this.eventService = eventService;
    }

    /**
     * POST /api/venues - Crear un nuevo venue
     */
    @PostMapping
    public ResponseEntity<VenueResponse> createVenue(@Valid @RequestBody VenueRequest request) {
        VenueResponse response = venueService.createVenue(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/venues/{id} - Obtener venue por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getVenueById(@PathVariable Long id) {
        VenueResponse response = venueService.getVenueById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/venues - Listar todos los venues o filtrar
     */
    @GetMapping
    public ResponseEntity<List<VenueResponse>> searchVenues(
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) String location) {

        if (minCapacity != null) {
            return ResponseEntity.ok(venueService.getVenuesByMinCapacity(minCapacity));
        }
        if (location != null) {
            return ResponseEntity.ok(venueService.getVenuesByLocation(location));
        }

        return ResponseEntity.ok(venueService.getAllVenues());
    }

    /**
     * GET /api/venues/{id}/events - Obtener todos los eventos de un venue
     */
    @GetMapping("/{id}/events")
    public ResponseEntity<List<EventResponse>> getVenueEvents(@PathVariable Long id) {
        List<EventResponse> events = eventService.getEventsByVenue(id);
        return ResponseEntity.ok(events);
    }

    /**
     * PUT /api/venues/{id} - Actualizar un venue
     */
    @PutMapping("/{id}")
    public ResponseEntity<VenueResponse> updateVenue(
            @PathVariable Long id,
            @Valid @RequestBody VenueRequest request) {
        VenueResponse response = venueService.updateVenue(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/venues/{id} - Eliminar un venue
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
