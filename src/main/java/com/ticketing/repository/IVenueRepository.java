package com.ticketing.repository;

import com.ticketing.dto.VenueDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface para el repositorio de venues.
 * 
 * Define el contrato para las operaciones CRUD de venues.
 * Aplica los principios I y D de SOLID.
 * 
 * @author Ticketing Team
 * @version 1.0
 */
public interface IVenueRepository {
    
    /**
     * Guarda un nuevo venue en el repositorio.
     * 
     * @param venue El venue a guardar
     * @return El venue guardado con su ID asignado
     */
    VenueDTO save(VenueDTO venue);
    
    /**
     * Busca un venue por su ID.
     * 
     * @param id El ID del venue a buscar
     * @return Optional conteniendo el venue si existe, o vacío si no
     */
    Optional<VenueDTO> findById(Long id);
    
    /**
     * Obtiene todos los venues del repositorio.
     * 
     * @return Lista de todos los venues
     */
    List<VenueDTO> findAll();
    
    /**
     * Actualiza un venue existente.
     * 
     * @param id El ID del venue a actualizar
     * @param venue El venue con los datos actualizados
     * @return Optional conteniendo el venue actualizado si existe, o vacío si no
     */
    Optional<VenueDTO> update(Long id, VenueDTO venue);
    
    /**
     * Elimina un venue por su ID.
     * 
     * @param id El ID del venue a eliminar
     * @return true si el venue fue eliminado, false si no existía
     */
    boolean deleteById(Long id);
    
    /**
     * Verifica si existe un venue con el ID especificado.
     * 
     * @param id El ID a verificar
     * @return true si existe, false si no
     */
    boolean existsById(Long id);
}