package com.eventcatalog.repository;

import com.eventcatalog.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad VenueEntity.
 * Extiende JpaRepository para heredar operaciones CRUD básicas.
 * 
 * Principios SOLID aplicados:
 * - ISP (Interface Segregation): Interfaz específica para operaciones de Venue
 * - DIP (Dependency Inversion): Los servicios dependen de esta abstracción, no de implementaciones concretas
 * - LSP (Liskov Substitution): Puede ser sustituido por cualquier implementación de JpaRepository
 * 
 * Spring Data JPA genera automáticamente la implementación en tiempo de ejecución.
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, Long> {

    /**
     * Busca un lugar por su nombre exacto.
     * Útil para validar duplicados antes de crear/actualizar.
     * 
     * @param name Nombre del lugar
     * @return Optional con el lugar si existe, vacío si no
     */
    Optional<VenueEntity> findByName(String name);

    /**
     * Busca un lugar por nombre ignorando mayúsculas/minúsculas.
     * 
     * @param name Nombre del lugar
     * @return Optional con el lugar si existe
     */
    Optional<VenueEntity> findByNameIgnoreCase(String name);

    /**
     * Verifica si existe un lugar con el nombre especificado.
     * Más eficiente que findByName cuando solo necesitamos verificar existencia.
     * 
     * @param name Nombre del lugar
     * @return true si existe, false si no
     */
    boolean existsByName(String name);

    /**
     * Verifica si existe un lugar con el nombre, excluyendo un ID específico.
     * Útil para validaciones en actualizaciones.
     * 
     * @param name Nombre del lugar
     * @param id ID a excluir de la búsqueda
     * @return true si existe otro lugar con ese nombre
     */
    boolean existsByNameAndIdNot(String name, Long id);

    /**
     * Busca todos los lugares de una ciudad específica.
     * 
     * @param city Ciudad a buscar
     * @return Lista de lugares en esa ciudad
     */
    List<VenueEntity> findByCity(String city);

    /**
     * Busca lugares por ciudad ignorando mayúsculas/minúsculas.
     * 
     * @param city Ciudad a buscar
     * @return Lista de lugares
     */
    List<VenueEntity> findByCityIgnoreCase(String city);

    /**
     * Busca lugares con capacidad mayor o igual a la especificada.
     * 
     * @param capacity Capacidad mínima
     * @return Lista de lugares que cumplen el criterio
     */
    List<VenueEntity> findByCapacityGreaterThanEqual(Integer capacity);

    /**
     * Busca lugares por nombre usando LIKE (búsqueda parcial).
     * El % debe ser incluido en el parámetro por el llamador.
     * 
     * @param name Patrón de nombre a buscar
     * @return Lista de lugares que coinciden
     */
    List<VenueEntity> findByNameContainingIgnoreCase(String name);

    /**
     * Query personalizada con JPQL para buscar lugares por ciudad y capacidad mínima.
     * Demuestra el uso de @Query para consultas más complejas.
     * 
     * @param city Ciudad a buscar
     * @param minCapacity Capacidad mínima
     * @return Lista de lugares que cumplen ambos criterios
     */
    @Query("SELECT v FROM VenueEntity v WHERE v.city = :city AND v.capacity >= :minCapacity ORDER BY v.capacity DESC")
    List<VenueEntity> findByCityAndMinCapacity(
        @Param("city") String city, 
        @Param("minCapacity") Integer minCapacity
    );

    /**
     * Query nativa SQL para obtener lugares con eventos activos.
     * Demuestra el uso de consultas SQL nativas cuando es necesario.
     * 
     * @return Lista de lugares que tienen al menos un evento programado
     */
    @Query(value = "SELECT DISTINCT v.* FROM venues v " +
                   "INNER JOIN events e ON v.id = e.venue_id " +
                   "WHERE e.status = 'ACTIVE' " +
                   "ORDER BY v.name", 
           nativeQuery = true)
    List<VenueEntity> findVenuesWithActiveEvents();

    /**
     * Cuenta la cantidad de lugares en una ciudad específica.
     * 
     * @param city Ciudad a contar
     * @return Número de lugares en esa ciudad
     */
    long countByCity(String city);
}