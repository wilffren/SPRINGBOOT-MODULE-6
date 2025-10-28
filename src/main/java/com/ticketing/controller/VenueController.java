package com.ticketing.controller;

import com.ticketing.dto.VenueDTO;
import com.ticketing.service.IVenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar venues.
 * 
 * Expone endpoints HTTP para realizar operaciones CRUD sobre venues.
 * Aplica los principios S y D de SOLID.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
@RestController
@RequestMapping("/venues")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Venues", description = "API para gestión de lugares/recintos")
public class VenueController {
    
    // Inyección de dependencias por constructor
    private final IVenueService venueService;
    
    /**
     * Crea un nuevo venue.
     * POST /api/venues
     */
    @PostMapping
    @Operation(summary = "Crear un nuevo venue", 
               description = "Crea un nuevo lugar/recinto en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Venue creado exitosamente",
                     content = @Content(schema = @Schema(implementation = VenueDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<VenueDTO> createVenue(
            @Valid @RequestBody VenueDTO venue) {
        
        log.info("POST /venues - Crear venue: {}", venue.getName());
        
        VenueDTO createdVenue = venueService.createVenue(venue);
        
        return new ResponseEntity<>(createdVenue, HttpStatus.CREATED);
    }
    
    /**
     * Obtiene todos los venues.
     * GET /api/venues
     */
    @GetMapping
    @Operation(summary = "Obtener todos los venues", 
               description = "Retorna una lista con todos los lugares/recintos del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de venues obtenida exitosamente")
    public ResponseEntity<List<VenueDTO>> getAllVenues() {
        log.info("GET /venues - Obtener todos los venues");
        
        List<VenueDTO> venues = venueService.getAllVenues();
        
        return ResponseEntity.ok(venues);
    }
    
    /**
     * Obtiene un venue por su ID.
     * GET /api/venues/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un venue por ID", 
               description = "Retorna un lugar/recinto específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venue encontrado",
                     content = @Content(schema = @Schema(implementation = VenueDTO.class))),
        @ApiResponse(responseCode = "404", description = "Venue no encontrado")
    })
    public ResponseEntity<VenueDTO> getVenueById(
            @Parameter(description = "ID del venue a buscar")
            @PathVariable Long id) {
        
        log.info("GET /venues/{} - Obtener venue por ID", id);
        
        VenueDTO venue = venueService.getVenueById(id);
        
        return ResponseEntity.ok(venue);
    }
    
    /**
     * Actualiza un venue existente.
     * PUT /api/venues/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un venue", 
               description = "Actualiza los datos de un lugar/recinto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venue actualizado exitosamente",
                     content = @Content(schema = @Schema(implementation = VenueDTO.class))),
        @ApiResponse(responseCode = "404", description = "Venue no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<VenueDTO> updateVenue(
            @Parameter(description = "ID del venue a actualizar")
            @PathVariable Long id,
            @Valid @RequestBody VenueDTO venue) {
        
        log.info("PUT /venues/{} - Actualizar venue", id);
        
        VenueDTO updatedVenue = venueService.updateVenue(id, venue);
        
        return ResponseEntity.ok(updatedVenue);
    }
    
    /**
     * Elimina un venue.
     * DELETE /api/venues/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un venue", 
               description = "Elimina un lugar/recinto del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Venue eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Venue no encontrado")
    })
    public ResponseEntity<Void> deleteVenue(
            @Parameter(description = "ID del venue a eliminar")
            @PathVariable Long id) {
        
        log.info("DELETE /venues/{} - Eliminar venue", id);
        
        venueService.deleteVenue(id);
        
        return ResponseEntity.noContent().build();
    }
}