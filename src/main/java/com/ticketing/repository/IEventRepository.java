package com.ticketing.repository;

import com.ticketing.dto.EventDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface para el repositorio de eventos.
 * 
 * Define el contrato para las operaciones CRUD de eventos.
 * Aplica el principio de Inversión de Dependencias (D de SOLID):
 * - Las capas superiores dependen de abstracciones, no de implementaciones concretas.
 * 
 * Aplica el principio de Segregación de Interfaces (I de SOLID):
 * - Interface específica solo con operaciones relacionadas a eventos.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
public interface IEventRepository {
    
    /**
     * Guarda un nuevo evento en el repositorio.
     * 
     * @param event El evento a guardar
     * @return El evento guardado con su ID asignado
     */
    EventDTO save(EventDTO event);
    
    /**
     * Busca un evento por su ID.
     * 
     * @param id El ID del evento a buscar
     * @return Optional conteniendo el evento si existe, o vacío si no
     */
    Optional<EventDTO> findById(Long id);
    
    /**
     * Obtiene todos los eventos del repositorio.
     * 
     * @return Lista de todos los eventos
     */
    List<EventDTO> findAll();
    
    /**
     * Actualiza un evento existente.
     * 
     * @param id El ID del evento a actualizar
     * @param event El evento con los datos actualizados
     * @return Optional conteniendo el evento actualizado si existe, o vacío si no
     */
    Optional<EventDTO> update(Long id, EventDTO event);
    
    /**
     * Elimina un evento por su ID.
     * 
     * @param id El ID del evento a eliminar
     * @return true si el evento fue eliminado, false si no existía
     */
    boolean deleteById(Long id);
    
    /**
     * Verifica si existe un evento con el ID especificado.
     * 
     * @param id El ID a verificar
     * @return true si existe, false si no
     */
    boolean existsById(Long id);
}