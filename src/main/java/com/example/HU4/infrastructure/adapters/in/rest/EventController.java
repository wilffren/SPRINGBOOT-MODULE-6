package com.example.HU4.infrastructure.adapters.in.rest;

import com.example.HU4.domain.model.Event;
import com.example.HU4.domain.ports.in.EventUseCase;
import com.example.HU4.infrastructure.dto.EventRequest;
import com.example.HU4.infrastructure.dto.EventResponse;
import com.example.HU4.infrastructure.validation.CreateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Events", description = "API para gestión de eventos")
@SecurityRequirement(name = "bearerAuth")
public class EventController {

    private final EventUseCase eventUseCase;

    public EventController(EventUseCase eventUseCase) {
        this.eventUseCase = eventUseCase;
    }

    @PostMapping
    @Operation(summary = "Crear evento", description = "Crea un nuevo evento en el sistema")
    public ResponseEntity<EventResponse> createEvent(@Validated(CreateGroup.class) @RequestBody EventRequest request) {
        Event domain = Event.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus())
                .venueId(request.getVenueId())
                .build();

        Event created = eventUseCase.createEvent(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping
    @Operation(summary = "Listar todos los eventos", description = "Obtiene la lista completa de eventos")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<Event> events = eventUseCase.getAllEvents();
        List<EventResponse> response = events.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener evento por ID", description = "Obtiene un evento específico por su ID")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return eventUseCase.getEventById(id)
                .map(event -> ResponseEntity.ok(toResponse(event)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar evento", description = "Actualiza un evento existente")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long id,
            @Validated(CreateGroup.class) @RequestBody EventRequest request) {
        Event domain = Event.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus())
                .venueId(request.getVenueId())
                .build();

        Event updated = eventUseCase.updateEvent(id, domain);
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar evento", description = "Elimina un evento del sistema")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventUseCase.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    private EventResponse toResponse(Event e) {
        return EventResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .status(e.getStatus())
                .venueId(e.getVenueId())
                .build();
    }
}