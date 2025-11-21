package com.example.HU4.domain.ports.out;

import com.example.HU4.domain.model.Task;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida - Repositorio de Tareas
 * Define el contrato para persistencia de tareas
 */
public interface TaskRepositoryPort {
    
    Task save(Task task);
    
    Optional<Task> findById(Long id);
    
    List<Task> findAll();
    
    List<Task> findByUserId(Long userId);
    
    List<Task> findByStatus(String status);
    
    List<Task> findByPriority(String priority);
    
    List<Task> findByFilters(Long userId, String status, String priority, LocalDate dueDate);
    
    List<Task> findByUserIdWithUser(Long userId);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
}