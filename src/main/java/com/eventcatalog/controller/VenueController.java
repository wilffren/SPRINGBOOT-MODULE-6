package com.eventcatalog.controller;

import com.eventcatalog.dto.VenueRequestDTO;
import com.eventcatalog.dto.VenueResponseDTO;
import com.eventcatalog.service.IVenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de lugares (Venues).
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Solo maneja las peticiones HTTP para venues
 * - DIP (Dependency Inversion): Depende de IVenueService (abstracción)
 * - OCP (Open/Closed): Abierto para extensión mediante nuevos endpoints
 * 
 * Endpoints disponibles:
 * - POST   /api/venues           : Crear lugar
 * - GET    /api/venues           : Obtener todos los lugares
 * - GET    /api/venues/{id}      : Obtener lugar por ID
 * - GET    /api/venues/city/{city} : Buscar por ciudad
 * - PUT    /api/venues/{id}      : Actualizar lugar
 * - DELETE /api/venues/{id}      : Eliminar lugar
 * 
 * @RestController: Combina @Controller + @ResponseBody
 * @RequestMapping: Define la ruta base para todos los endpoints
 * @CrossOrigin: Habilita CORS para este controlador
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@RestController
@RequestMapping("/venues")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200") // Permitir peticiones desde Angular
public class VenueController {

    // Inyección de dependencias del servicio
    private final IVenueService venueService;

    /**
     * Crea un nuevo lugar.
     * 
     * TASK 2: @Valid activa las validaciones del DTO
     * 
     * @param venueRequest DTO con datos del lugar a crear
     * @return ResponseEntity con el lugar creado (HTTP 201)
     * 
     * Ejemplo de request body:
     * {
     *   "name": "Teatro Nacional",
     *   "address": "Calle 71 #10-25",
     *   "city": "Bogotá",
     *   "capacity": 1500,
     *   "description": "Teatro principal de la ciudad"
     * }
     */
    @PostMapping
    public ResponseEntity<VenueResponseDTO> create(
            @Valid @RequestBody VenueRequestDTO venueRequest) {
        
        log.info("POST /venues - Creando lugar: {}", venueRequest.getName());
        VenueResponseDTO createdVenue = venueService.create(venueRequest);
        
        return new ResponseEntity<>(createdVenue, HttpStatus.CREATED);
    }

    /**
     * Obtiene todos los lugares registrados.
     * 
     * @return ResponseEntity con lista de lugares (HTTP 200)
     * 
     * Ejemplo de respuesta:
     * [
     *   {
     *     "id": 1,
     *     "name": "Teatro Nacional",
     *     "address": "Calle 71 #10-25",
     *     "city": "Bogotá",
     *     "capacity": 1500,
     *     "eventCount": 5
     *   }
     * ]
     */
    @GetMapping
    public ResponseEntity<List<VenueResponseDTO>> findAll() {
        log.info("GET /venues - Obteniendo todos los lugares");
        List<VenueResponseDTO> venues = venueService.findAll();
        
        return ResponseEntity.ok(venues);
    }

    /**
     * Obtiene un lugar por su ID.
     * 
     * @param id ID del lugar
     * @return ResponseEntity con el lugar encontrado (HTTP 200)
     * @throws VenueNotFoundException si no se encuentra (HTTP 404)
     * 
     * Ejemplo: GET /api/venues/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<VenueResponseDTO> findById(@PathVariable Long id) {
        log.info("GET /venues/{} - Buscando lugar por ID", id);
        VenueResponseDTO venue = venueService.findById(id);
        
        return ResponseEntity.ok(venue);
    }

    /**
     * Busca lugares por ciudad.
     * 
     * @param city Ciudad a buscar
     * @return ResponseEntity con lista de lugares (HTTP 200)
     * 
     * Ejemplo: GET /api/venues/city/Bogotá
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<VenueResponseDTO>> findByCity(@PathVariable String city) {
        log.info("GET /venues/city/{} - Buscando lugares por ciudad", city);
        List<VenueResponseDTO> venues = venueService.findByCity(city);
        
        return ResponseEntity.ok(venues);
    }

    /**
     * Actualiza un lugar existente.
     * 
     * TASK 2: Valida que no haya duplicados al actualizar
     * 
     * @param id ID del lugar a actualizar
     * @param venueRequest DTO con nuevos datos
     * @return ResponseEntity con el lugar actualizado (HTTP 200)
     * @throws VenueNotFoundException si no se encuentra (HTTP 404)
     * @throws DuplicateVenueException si el nombre ya existe (HTTP 409)
     * 
     * Ejemplo: PUT /api/venues/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<VenueResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody VenueRequestDTO venueRequest) {
        
        log.info("PUT /venues/{} - Actualizando lugar", id);
        VenueResponseDTO updatedVenue = venueService.update(id, venueRequest);
        
        return ResponseEntity.ok(updatedVenue);
    }

    /**
     * Elimina un lugar por su ID.
     * 
     * @param id ID del lugar a eliminar
     * @return ResponseEntity sin contenido (HTTP 204)
     * @throws VenueNotFoundException si no se encuentra (HTTP 404)
     * 
     * Ejemplo: DELETE /api/venues/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /venues/{} - Eliminando lugar", id);
        venueService.delete(id);
        
        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica si existe un lugar con el nombre especificado.
     * Endpoint útil para validaciones en el frontend.
     * 
     * @param name Nombre del lugar
     * @return ResponseEntity con booleano (HTTP 200)
     * 
     * Ejemplo: GET /api/venues/exists?name=Teatro Nacional
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByName(@RequestParam String name) {
        log.info("GET /venues/exists?name={}", name);
        boolean exists = venueService.existsByName(name);
        
        return ResponseEntity.ok(exists);
    }
}