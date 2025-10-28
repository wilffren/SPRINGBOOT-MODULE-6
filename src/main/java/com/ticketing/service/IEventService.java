package com.ticketing.service;

import com.ticketing.dto.EventDTO;

import java.util.List;

/**
 * Interface del servicio de eventos.
 * 
 * Define el contrato para la lógica de negocio relacionada con eventos.
 * 
 * Aplica el principio de Abierto/Cerrado (O de SOLID):
 * - Abierto para extensión: Se pueden crear nuevas implementaciones.
 * - Cerrado para modificación: La interface no cambia al agregar nueva lógica.
 * 
 * Aplica el principio de Inversión de Dependencias (D de SOLID):
 * - Los controladores dependen de esta abstracción, no de implementaciones concretas.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
public interface IEventService {
    
    /**
     * Crea un nuevo evento en el sistema.
     * 
     * @param event Los datos del evento a crear
     * @return El evento creado con su ID asignado
     */
    EventDTO createEvent(EventDTO event);
    
    /**
     * Obtiene un evento por su ID.
     * 
     * @param id El ID del evento a buscar
     * @return El evento encontrado
     * @throws com.ticketing.exception.ResourceNotFoundException si el evento no existe
     */
    EventDTO getEventById(Long id);
    
    /**
     * Obtiene todos los eventos del sistema.
     * 
     * @return Lista de todos los eventos
     */
    List<EventDTO> getAllEvents();
    
    /**
     * Actualiza un evento existente.
     * 
     * @param id El ID del evento a actualizar
     * @param event Los nuevos datos del evento
     * @return El evento actualizado
     * @throws com.ticketing.exception.ResourceNotFoundException si el evento no existe
     */
    EventDTO updateEvent(Long id, EventDTO event);
    
    /**
     * Elimina un evento del sistema.
     * 
     * @param id El ID del evento a eliminar
     * @throws com.ticketing.exception.ResourceNotFoundException si el evento no existe
     */
    void deleteEvent(Long id);
}