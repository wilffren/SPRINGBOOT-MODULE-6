package com.example.HU4.infrastructure.adapters.in.rest;

import com.example.HU4.domain.model.Venue;
import com.example.HU4.domain.ports.in.VenueUseCase;
import com.example.HU4.infrastructure.dto.VenueRequest;
import com.example.HU4.infrastructure.dto.VenueResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/venues")
@Tag(name = "Venues", description = "API para gestión de venues (lugares/locaciones)")
@SecurityRequirement(name = "bearerAuth")
public class VenueController {

    private final VenueUseCase venueUseCase;

    public VenueController(VenueUseCase venueUseCase) {
        this.venueUseCase = venueUseCase;
    }

    @PostMapping
    @Operation(summary = "Crear venue", description = "Crea un nuevo venue en el sistema")
    public ResponseEntity<VenueResponse> createVenue(@Valid @RequestBody VenueRequest request) {
        Venue domain = Venue.builder()
                .name(request.getName())
                .location(request.getLocation())
                .capacity(request.getCapacity())
                .description(request.getDescription())
                .build();

        Venue created = venueUseCase.createVenue(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping
    @Operation(summary = "Listar todos los venues", description = "Obtiene la lista completa de venues")
    public ResponseEntity<List<VenueResponse>> getAllVenues() {
        List<Venue> venues = venueUseCase.getAllVenues();
        List<VenueResponse> response = venues.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener venue por ID", description = "Obtiene un venue específico por su ID")
    public ResponseEntity<VenueResponse> getVenueById(@PathVariable Long id) {
        return venueUseCase.getVenueById(id)
                .map(venue -> ResponseEntity.ok(toResponse(venue)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar venue", description = "Actualiza un venue existente")
    public ResponseEntity<VenueResponse> updateVenue(
            @PathVariable Long id,
            @Valid @RequestBody VenueRequest request) {
        Venue domain = Venue.builder()
                .name(request.getName())
                .location(request.getLocation())
                .capacity(request.getCapacity())
                .description(request.getDescription())
                .build();

        Venue updated = venueUseCase.updateVenue(id, domain);
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venue", description = "Elimina un venue del sistema")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueUseCase.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }

    private VenueResponse toResponse(Venue v) {
        return VenueResponse.builder()
                .id(v.getId())
                .name(v.getName())
                .location(v.getLocation())
                .capacity(v.getCapacity())
                .description(v.getDescription())
                .build();
    }
}
