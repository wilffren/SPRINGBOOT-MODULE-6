package com.eventcatalog.controller;

import com.eventcatalog.dto.EventRequestDTO;
import com.eventcatalog.dto.EventResponseDTO;
import com.eventcatalog.service.IEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de eventos.
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Solo maneja las peticiones HTTP para eventos
 * - DIP (Dependency Inversion): Depende de IEventService (abstracción)
 * - OCP (Open/Closed): Abierto para extensión mediante nuevos endpoints
 * 
 * Endpoints disponibles:
 * - POST   /api/events              : Crear evento
 * - GET    /api/events              : Obtener todos los eventos
 * - GET    /api/events/{id}         : Obtener evento por ID
 * - GET    /api/events/city/{city}  : Buscar por ciudad
 * - GET    /api/events/category/{category} : Buscar por categoría
 * - GET    /api/events/upcoming     : Obtener eventos futuros
 * - PUT    /api/events/{id}         : Actualizar evento
 * - DELETE /api/events/{id}         : Eliminar evento
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

    private final IEventService eventService;

    /**
     * Crea un nuevo evento.
     * 
     * TASK 2: @Valid activa todas las validaciones del DTO
     * - @NotBlank en campos de texto
     * - @Future en fecha del evento
     * - @DecimalMin/@DecimalMax en precio
     * - @Min/@Max en tickets disponibles
     * 
     * @param eventRequest DTO con datos del evento
     * @return ResponseEntity con el evento creado (HTTP 201)
     * 
     * Ejemplo de request body:
     * {
     *   "name": "Concierto Rock en Vivo",
     *   "description": "Gran concierto de rock con las mejores bandas...",
     *   "category": "Concierto",
     *   "city": "Bogotá",
     *   "eventDate": "2025-12-15T20:00:00",
     *   "price": 150000.00,
     *   "availableTickets": 500,
     *   "imageUrl": "https://example.com/images/concert.jpg",
     *   "venueId": 1
     * }
     */
    @PostMapping
    public ResponseEntity<EventResponseDTO> create(
            @Valid @RequestBody EventRequestDTO eventRequest) {
        
        log.info("POST /events - Creando evento: {}", eventRequest.getName());
        EventResponseDTO createdEvent = eventService.create(eventRequest);
        
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    /**
     * Obtiene todos los eventos registrados.
     * 
     * @return ResponseEntity con lista de eventos (HTTP 200)
     * 
     * Respuesta incluye información del venue asociado:
     * [
     *   {
     *     "id": 1,
     *     "name": "Concierto Rock",
     *     "category": "Concierto",
     *     "eventDate": "2025-12-15T20:00:00",
     *     "price": 150000.00,
     *     "venue": {
     *       "id": 1,
     *       "name": "Teatro Nacional",
     *       "city": "Bogotá"
     *     }
     *   }
     * ]
     */
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> findAll() {
        log.info("GET /events - Obteniendo todos los eventos");
        List<EventResponseDTO> events = eventService.findAll();
        
        return ResponseEntity.ok(events);
    }

    /**
     * Obtiene un evento por su ID.
     * 
     * @param id ID del evento
     * @return ResponseEntity con el evento encontrado (HTTP 200)
     * @throws EventNotFoundException si no se encuentra (HTTP 404)
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> findById(@PathVariable Long id) {
        log.info("GET /events/{} - Buscando evento por ID", id);
        EventResponseDTO event = eventService.findById(id);
        
        return ResponseEntity.ok(event);
    }

    /**
     * Busca eventos por ciudad.
     * 
     * @param city Ciudad a buscar
     * @return ResponseEntity con lista de eventos (HTTP 200)
     * 
     * Ejemplo: GET /api/events/city/Bogotá
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<EventResponseDTO>> findByCity(@PathVariable String city) {
        log.info("GET /events/city/{} - Buscando eventos por ciudad", city);
        List<EventResponseDTO> events = eventService.findByCity(city);
        
        return ResponseEntity.ok(events);
    }

    /**
     * Busca eventos por categoría.
     * 
     * @param category Categoría a buscar
     * @return ResponseEntity con lista de eventos (HTTP 200)
     * 
     * Ejemplo: GET /api/events/category/Concierto
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<EventResponseDTO>> findByCategory(@PathVariable String category) {
        log.info("GET /events/category/{} - Buscando eventos por categoría", category);
        List<EventResponseDTO> events = eventService.findByCategory(category);
        
        return ResponseEntity.ok(events);
    }

    /**
     * Obtiene eventos futuros (después de la fecha actual).
     * Útil para mostrar solo eventos próximos en el frontend.
     * 
     * @return ResponseEntity con lista de eventos futuros (HTTP 200)
     * 
     * Ejemplo: GET /api/events/upcoming
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponseDTO>> findUpcomingEvents() {
        log.info("GET /events/upcoming - Obteniendo eventos futuros");
        List<EventResponseDTO> events = eventService.findUpcomingEvents();
        
        return ResponseEntity.ok(events);
    }

    /**
     * Actualiza un evento existente.
     * 
     * TASK 2: 
     * - Valida que no haya duplicados al actualizar
     * - Valida todas las constraints del DTO
     * 
     * @param id ID del evento a actualizar
     * @param eventRequest DTO con nuevos datos
     * @return ResponseEntity con el evento actualizado (HTTP 200)
     * @throws EventNotFoundException si no se encuentra (HTTP 404)
     * @throws DuplicateEventException si el nombre ya existe (HTTP 409)
     * @throws VenueNotFoundException si el venue no existe (HTTP 404)
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EventRequestDTO eventRequest) {
        
        log.info("PUT /events/{} - Actualizando evento", id);
        EventResponseDTO updatedEvent = eventService.update(id, eventRequest);
        
        return ResponseEntity.ok(updatedEvent);
    }

    /**
     * Elimina un evento por su ID.
     * 
     * @param id ID del evento a eliminar
     * @return ResponseEntity sin contenido (HTTP 204)
     * @throws EventNotFoundException si no se encuentra (HTTP 404)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /events/{} - Eliminando evento", id);
        eventService.delete(id);
        
        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica si existe un evento con el nombre especificado.
     * Endpoint útil para validaciones en tiempo real en el frontend.
     * 
     * @param name Nombre del evento
     * @return ResponseEntity con booleano (HTTP 200)
     * 
     * Ejemplo: GET /api/events/exists?name=Concierto Rock
     * Respuesta: true o false
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByName(@RequestParam String name) {
        log.info("GET /events/exists?name={}", name);
        boolean exists = eventService.existsByName(name);
        
        return ResponseEntity.ok(exists);
    }
}