package com.eventcatalog.repository;

import com.eventcatalog.entity.EventEntity;
import com.eventcatalog.entity.EventEntity.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad EventEntity.
 * Extiende JpaRepository para heredar operaciones CRUD básicas.
 * 
 * Principios SOLID aplicados:
 * - ISP (Interface Segregation): Interfaz específica para operaciones de Event
 * - DIP (Dependency Inversion): Los servicios dependen de esta abstracción
 * - OCP (Open/Closed): Abierto para extensión mediante nuevos métodos de consulta
 * 
 * Spring Data JPA proporciona la implementación automática.
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Repository
public interface EventRepository extends 
        JpaRepository<EventEntity, Long>,
        org.springframework.data.jpa.repository.JpaSpecificationExecutor<EventEntity> {

    // ========================================
    // MÉTODOS DE VALIDACIÓN
    // ========================================

    /**
     * Busca un evento por su nombre exacto.
     * Útil para validar unicidad del nombre.
     * 
     * @param name Nombre del evento
     * @return Optional con el evento si existe
     */
    Optional<EventEntity> findByName(String name);

    /**
     * Verifica si existe un evento con el nombre especificado.
     * Más eficiente que findByName para validaciones.
     * 
     * @param name Nombre del evento
     * @return true si existe, false si no
     */
    boolean existsByName(String name);

    /**
     * Verifica si existe un evento con el nombre, excluyendo un ID específico.
     * Útil para validaciones en actualizaciones (evitar conflicto con el mismo registro).
     * 
     * @param name Nombre del evento
     * @param id ID a excluir
     * @return true si existe otro evento con ese nombre
     */
    boolean existsByNameAndIdNot(String name, Long id);

    // ========================================
    // BÚSQUEDAS BÁSICAS
    // ========================================

    /**
     * Busca todos los eventos de una categoría específica.
     * 
     * @param category Categoría del evento
     * @return Lista de eventos de esa categoría
     */
    List<EventEntity> findByCategory(String category);

    /**
     * Busca todos los eventos de una ciudad específica.
     * 
     * @param city Ciudad del evento
     * @return Lista de eventos en esa ciudad
     */
    List<EventEntity> findByCity(String city);

    /**
     * Busca eventos por estado.
     * 
     * @param status Estado del evento
     * @return Lista de eventos con ese estado
     */
    List<EventEntity> findByStatus(EventStatus status);

    /**
     * Busca eventos cuyo nombre contenga el texto especificado (case insensitive).
     * 
     * @param name Texto a buscar en el nombre
     * @return Lista de eventos que coinciden
     */
    List<EventEntity> findByNameContainingIgnoreCase(String name);

    // ========================================
    // BÚSQUEDAS POR FECHA
    // ========================================

    /**
     * Busca eventos programados después de una fecha específica.
     * 
     * @param date Fecha de referencia
     * @return Lista de eventos futuros
     */
    List<EventEntity> findByEventDateAfter(LocalDateTime date);

    /**
     * Busca eventos programados antes de una fecha específica.
     * 
     * @param date Fecha de referencia
     * @return Lista de eventos pasados
     */
    List<EventEntity> findByEventDateBefore(LocalDateTime date);

    /**
     * Busca eventos en un rango de fechas.
     * 
     * @param startDate Fecha de inicio
     * @param endDate Fecha de fin
     * @return Lista de eventos en el rango
     */
    List<EventEntity> findByEventDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // ========================================
    // CONSULTAS COMBINADAS
    // ========================================

    /**
     * Busca eventos por ciudad y categoría.
     * 
     * @param city Ciudad
     * @param category Categoría
     * @return Lista de eventos que cumplen ambos criterios
     */
    List<EventEntity> findByCityAndCategory(String city, String category);

    /**
     * Busca eventos activos en una ciudad específica.
     * 
     * @param city Ciudad
     * @param status Estado
     * @return Lista de eventos activos en esa ciudad
     */
    List<EventEntity> findByCityAndStatus(String city, EventStatus status);

    /**
     * Busca eventos futuros por categoría.
     * 
     * @param category Categoría
     * @param date Fecha de referencia (normalmente LocalDateTime.now())
     * @return Lista de eventos futuros de esa categoría
     */
    List<EventEntity> findByCategoryAndEventDateAfter(String category, LocalDateTime date);

    // ========================================
    // CONSULTAS CON PAGINACIÓN
    // ========================================

    /**
     * Busca todos los eventos con paginación.
     * Preparado para TASK 3 (Paginación).
     * 
     * @param pageable Configuración de paginación y ordenamiento
     * @return Página de eventos
     */
    Page<EventEntity> findAll(Pageable pageable);

    /**
     * Busca eventos por ciudad con paginación.
     * 
     * @param city Ciudad
     * @param pageable Configuración de paginación
     * @return Página de eventos
     */
    Page<EventEntity> findByCity(String city, Pageable pageable);

    /**
     * Busca eventos por categoría con paginación.
     * 
     * @param category Categoría
     * @param pageable Configuración de paginación
     * @return Página de eventos
     */
    Page<EventEntity> findByCategory(String category, Pageable pageable);

    /**
     * Busca eventos futuros con paginación.
     * 
     * @param date Fecha de referencia
     * @param pageable Configuración de paginación
     * @return Página de eventos futuros
     */
    Page<EventEntity> findByEventDateAfter(LocalDateTime date, Pageable pageable);

    // ========================================
    // CONSULTAS PERSONALIZADAS CON @Query
    // ========================================

    /**
     * Query JPQL para búsqueda avanzada con múltiples filtros opcionales.
     * Preparado para TASK 3 (Filtros).
     * 
     * @param city Ciudad (puede ser null)
     * @param category Categoría (puede ser null)
     * @param startDate Fecha de inicio (puede ser null)
     * @param pageable Configuración de paginación
     * @return Página de eventos filtrados
     */
    @Query("SELECT e FROM EventEntity e WHERE " +
           "(:city IS NULL OR e.city = :city) AND " +
           "(:category IS NULL OR e.category = :category) AND " +
           "(:startDate IS NULL OR e.eventDate >= :startDate) AND " +
           "e.status = 'ACTIVE' " +
           "ORDER BY e.eventDate ASC")
    Page<EventEntity> findByFilters(
        @Param("city") String city,
        @Param("category") String category,
        @Param("startDate") LocalDateTime startDate,
        Pageable pageable
    );

    /**
     * Query para obtener eventos con entradas disponibles.
     * 
     * @return Lista de eventos con tickets disponibles
     */
    @Query("SELECT e FROM EventEntity e WHERE e.availableTickets > 0 AND e.status = 'ACTIVE'")
    List<EventEntity> findEventsWithAvailableTickets();

    /**
     * Query nativa para obtener estadísticas de eventos por categoría.
     * 
     * @return Lista de objetos con categoría y conteo
     */
    @Query(value = "SELECT category, COUNT(*) as total FROM events GROUP BY category ORDER BY total DESC", 
           nativeQuery = true)
    List<Object[]> getEventStatisticsByCategory();

    /**
     * Cuenta eventos activos en una ciudad.
     * 
     * @param city Ciudad
     * @param status Estado
     * @return Número de eventos
     */
    long countByCityAndStatus(String city, EventStatus status);

    /**
     * Busca eventos de un lugar específico.
     * 
     * @param venueId ID del lugar
     * @return Lista de eventos en ese lugar
     */
    List<EventEntity> findByVenueId(Long venueId);

    /**
     * Busca eventos futuros de un lugar específico.
     * 
     * @param venueId ID del lugar
     * @param date Fecha de referencia
     * @return Lista de eventos futuros
     */
    List<EventEntity> findByVenueIdAndEventDateAfter(Long venueId, LocalDateTime date);
}