package com.example.HU4.infrastructure.adapters.in.rest;

import com.example.HU4.domain.model.Venue;
import com.example.HU4.domain.ports.in.VenueUseCase;
import com.example.HU4.infrastructure.dto.VenueRequest;
import com.example.HU4.infrastructure.dto.VenueResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueUseCase venueUseCase;

    public VenueController(VenueUseCase venueUseCase) {
        this.venueUseCase = venueUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
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
    public ResponseEntity<List<VenueResponse>> getAllVenues() {
        List<Venue> venues = venueUseCase.getAllVenues();
        List<VenueResponse> response = venues.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
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
