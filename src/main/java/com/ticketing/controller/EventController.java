package com.ticketing.controller;

import com.ticketing.dto.EventDTO;
import com.ticketing.service.IEventService;
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
 * Controlador REST para gestionar eventos.
 * 
 * Expone endpoints HTTP para realizar operaciones CRUD sobre eventos.
 * 
 * Aplica el principio de Responsabilidad Única (S de SOLID):
 * - Solo se encarga de manejar peticiones HTTP y delegar al servicio.
 * 
 * Aplica el principio de Inversión de Dependencias (D de SOLID):
 * - Depende de la abstracción IEventService, no de la implementación.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Eventos", description = "API para gestión de eventos")
public class EventController {
    
    // Inyección de dependencias por constructor
    private final IEventService eventService;
    
    /**
     * Crea un nuevo evento.
     * POST /api/events
     * 
     * @param event Los datos del evento a crear
     * @return ResponseEntity con el evento creado y código HTTP 201
     */
    @PostMapping
    @Operation(summary = "Crear un nuevo evento", 
               description = "Crea un nuevo evento en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evento creado exitosamente",
                     content = @Content(schema = @Schema(implementation = EventDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EventDTO> createEvent(
            @Valid @RequestBody EventDTO event) {
        
        log.info("POST /events - Crear evento: {}", event.getName());
        
        EventDTO createdEvent = eventService.createEvent(event);
        
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }
    
    /**
     * Obtiene todos los eventos.
     * GET /api/events
     * 
     * @return ResponseEntity con lista de eventos y código HTTP 200
     */
    @GetMapping
    @Operation(summary = "Obtener todos los eventos", 
               description = "Retorna una lista con todos los eventos del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de eventos obtenida exitosamente")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        log.info("GET /events - Obtener todos los eventos");
        
        List<EventDTO> events = eventService.getAllEvents();
        
        return ResponseEntity.ok(events);
    }
    
    /**
     * Obtiene un evento por su ID.
     * GET /api/events/{id}
     * 
     * @param id El ID del evento a buscar
     * @return ResponseEntity con el evento encontrado y código HTTP 200
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un evento por ID", 
               description = "Retorna un evento específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento encontrado",
                     content = @Content(schema = @Schema(implementation = EventDTO.class))),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    public ResponseEntity<EventDTO> getEventById(
            @Parameter(description = "ID del evento a buscar")
            @PathVariable Long id) {
        
        log.info("GET /events/{} - Obtener evento por ID", id);
        
        EventDTO event = eventService.getEventById(id);
        
        return ResponseEntity.ok(event);
    }
    
    /**
     * Actualiza un evento existente.
     * PUT /api/events/{id}
     * 
     * @param id El ID del evento a actualizar
     * @param event Los nuevos datos del evento
     * @return ResponseEntity con el evento actualizado y código HTTP 200
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un evento", 
               description = "Actualiza los datos de un evento existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente",
                     content = @Content(schema = @Schema(implementation = EventDTO.class))),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EventDTO> updateEvent(
            @Parameter(description = "ID del evento a actualizar")
            @PathVariable Long id,
            @Valid @RequestBody EventDTO event) {
        
        log.info("PUT /events/{} - Actualizar evento", id);
        
        EventDTO updatedEvent = eventService.updateEvent(id, event);
        
        return ResponseEntity.ok(updatedEvent);
    }
    
    /**
     * Elimina un evento.
     * DELETE /api/events/{id}
     * 
     * @param id El ID del evento a eliminar
     * @return ResponseEntity sin contenido y código HTTP 204
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un evento", 
               description = "Elimina un evento del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Evento eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "ID del evento a eliminar")
            @PathVariable Long id) {
        
        log.info("DELETE /events/{} - Eliminar evento", id);
        
        eventService.deleteEvent(id);
        
        return ResponseEntity.noContent().build();
    }
}