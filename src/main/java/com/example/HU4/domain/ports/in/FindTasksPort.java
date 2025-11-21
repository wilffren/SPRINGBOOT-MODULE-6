package com.example.HU4.domain.ports.in;

import com.example.HU4.domain.model.Task;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada - Buscar Tareas
 * Define el contrato para consultar tareas con filtros
 */
public interface FindTasksPort {
    
    /**
     * Busca una tarea por su ID
     */
    Optional<Task> findById(Long id);
    
    /**
     * Obtiene todas las tareas
     */
    List<Task> findAll();
    
    /**
     * Busca tareas por usuario
     */
    List<Task> findByUserId(Long userId);
    
    /**
     * Busca tareas por estado
     */
    List<Task> findByStatus(String status);
    
    /**
     * Busca tareas por prioridad
     */
    List<Task> findByPriority(String priority);
    
    /**
     * Busca tareas con filtros dinámicos
     */
    List<Task> findByFilters(Long userId, String status, String priority, LocalDate dueDate);
    
    /**
     * Busca tareas de un usuario con carga optimizada (evita N+1)
     */
    List<Task> findByUserIdOptimized(Long userId);
}