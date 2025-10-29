package com.eventcatalog.service;

import com.eventcatalog.dto.VenueRequestDTO;
import com.eventcatalog.dto.VenueRequestDTO;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de lugares (Venues).
 * 
 * Principios SOLID aplicados:
 * - DIP (Dependency Inversion Principle): Los controladores dependen de esta abstracción,
 *   no de la implementación concreta
 * - ISP (Interface Segregation Principle): Interfaz específica para operaciones de Venue
 * - OCP (Open/Closed Principle): Abierto para extensión mediante nuevas implementaciones
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
public interface IVenueService {

    /**
     * Crea un nuevo lugar.
     * 
     * @param venueRequest DTO con los datos del lugar a crear
     * @return DTO con la información del lugar creado
     * @throws DuplicateVenueException si ya existe un lugar con ese nombre
     */
    VenueResponseDTO create(VenueRequestDTO venueRequest);

    /**
     * Obtiene un lugar por su ID.
     * 
     * @param id ID del lugar
     * @return DTO con la información del lugar
     * @throws VenueNotFoundException si no se encuentra el lugar
     */
    VenueResponseDTO findById(Long id);

    /**
     * Obtiene todos los lugares registrados.
     * 
     * @return Lista de lugares
     */
    List<VenueResponseDTO> findAll();

    /**
     * Busca lugares por ciudad.
     * 
     * @param city Ciudad a buscar
     * @return Lista de lugares en esa ciudad
     */
    List<VenueResponseDTO> findByCity(String city);

    /**
     * Actualiza un lugar existente.
     * 
     * @param id ID del lugar a actualizar
     * @param venueRequest DTO con los nuevos datos
     * @return DTO con la información actualizada
     * @throws VenueNotFoundException si no se encuentra el lugar
     * @throws DuplicateVenueException si el nuevo nombre ya existe
     */
    VenueResponseDTO update(Long id, VenueRequestDTO venueRequest);

    /**
     * Elimina un lugar por su ID.
     * 
     * @param id ID del lugar a eliminar
     * @throws VenueNotFoundException si no se encuentra el lugar
     */
    void delete(Long id);

    /**
     * Verifica si existe un lugar con el nombre especificado.
     * 
     * @param name Nombre del lugar
     * @return true si existe, false si no
     */
    boolean existsByName(String name);
}