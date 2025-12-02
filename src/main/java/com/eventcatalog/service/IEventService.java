package com.eventcatalog.service;

import com.eventcatalog.dto.EventFilterDTO;
import com.eventcatalog.dto.EventRequestDTO;
import com.eventcatalog.dto.EventResponseDTO;
import com.eventcatalog.dto.PageResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de eventos.
 * 
 * Principios SOLID aplicados:
 * - DIP (Dependency Inversion Principle): Los controladores dependen de esta abstracción
 * - ISP (Interface Segregation Principle): Interfaz específica para operaciones de Event
 * - OCP (Open/Closed Principle): Abierto para extensión mediante nuevas implementaciones
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
public interface IEventService {

    /**
     * Crea un nuevo evento.
     * 
     * TASK 2: Valida duplicados en nombres de eventos
     * 
     * @param eventRequest DTO con los datos del evento a crear
     * @return DTO con la información del evento creado
     * @throws DuplicateEventException si ya existe un evento con ese nombre
     * @throws VenueNotFoundException si no se encuentra el lugar especificado
     */
    EventResponseDTO create(EventRequestDTO eventRequest);

    /**
     * Obtiene un evento por su ID.
     * 
     * @param id ID del evento
     * @return DTO con la información del evento
     * @throws EventNotFoundException si no se encuentra el evento
     */
    EventResponseDTO findById(Long id);

    /**
     * Obtiene todos los eventos registrados.
     * 
     * @return Lista de eventos
     */
    List<EventResponseDTO> findAll();

    /**
     * Obtiene eventos con paginación y ordenamiento.
     * 
     * TASK 3: Implementa paginación con Pageable.
     * 
     * @param pageable Configuración de paginación (page, size, sort)
     * @return Página de eventos
     */
    PageResponseDTO<EventResponseDTO> findAllPaginated(Pageable pageable);

    /**
     * Busca eventos aplicando filtros opcionales con paginación.
     * 
     * TASK 3: Filtros opcionales por ciudad, categoría, fechaInicio.
     * 
     * @param filters DTO con filtros opcionales
     * @param pageable Configuración de paginación
     * @return Página de eventos filtrados
     */
    PageResponseDTO<EventResponseDTO> findWithFilters(
        EventFilterDTO filters,
        Pageable pageable
    );

    /**
     * Busca eventos por ciudad.
     * 
     * @param city Ciudad a buscar
     * @return Lista de eventos en esa ciudad
     */
    List<EventResponseDTO> findByCity(String city);

    /**
     * Busca eventos por categoría.
     * 
     * @param category Categoría a buscar
     * @return Lista de eventos de esa categoría
     */
    List<EventResponseDTO> findByCategory(String category);

    /**
     * Obtiene eventos futuros (después de la fecha actual).
     * 
     * @return Lista de eventos futuros
     */
    List<EventResponseDTO> findUpcomingEvents();

    /**
     * Actualiza un evento existente.
     * 
     * @param id ID del evento a actualizar
     * @param eventRequest DTO con los nuevos datos
     * @return DTO con la información actualizada
     * @throws EventNotFoundException si no se encuentra el evento
     * @throws DuplicateEventException si el nuevo nombre ya existe
     * @throws VenueNotFoundException si el venue especificado no existe
     */
    EventResponseDTO update(Long id, EventRequestDTO eventRequest);

    /**
     * Elimina un evento por su ID.
     * 
     * @param id ID del evento a eliminar
     * @throws EventNotFoundException si no se encuentra el evento
     */
    void delete(Long id);

    /**
     * Verifica si existe un evento con el nombre especificado.
     * 
     * @param name Nombre del evento
     * @return true si existe, false si no
     */
    boolean existsByName(String name);
}