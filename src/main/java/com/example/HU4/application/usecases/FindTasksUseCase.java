package com.example.HU4.application.usecases;

import com.example.HU4.domain.model.Task;
import com.example.HU4.domain.ports.in.FindTasksPort;
import com.example.HU4.domain.ports.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * TASK 3: Caso de uso para consultar tareas
 * Usa @Transactional(readOnly = true) para optimizar lecturas
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FindTasksUseCase implements FindTasksPort {
    
    private final TaskRepositoryPort taskRepository;
    
    /**
     * TASK 3: Transacción de solo lectura
     * readOnly = true mejora rendimiento en consultas
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Task> findById(Long id) {
        log.debug("Buscando tarea por ID: {}", id);
        return taskRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Task> findAll() {
        log.debug("Consultando todas las tareas");
        return taskRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Task> findByUserId(Long userId) {
        log.debug("Buscando tareas del usuario: {}", userId);
        return taskRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Task> findByStatus(String status) {
        log.debug("Buscando tareas con estado: {}", status);
        return taskRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Task> findByPriority(String priority) {
        log.debug("Buscando tareas con prioridad: {}", priority);
        return taskRepository.findByPriority(priority);
    }
    
    /**
     * TASK 2: Usa filtros dinámicos con Specifications
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> findByFilters(Long userId, String status, String priority, LocalDate dueDate) {
        log.info("Buscando tareas con filtros - userId: {}, status: {}, priority: {}, dueDate: {}", 
            userId, status, priority, dueDate);
        return taskRepository.findByFilters(userId, status, priority, dueDate);
    }
    
    /**
     * TASK 2: Consulta optimizada que resuelve N+1
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> findByUserIdOptimized(Long userId) {
        log.info("Buscando tareas del usuario {} con carga optimizada", userId);
        return taskRepository.findByUserIdWithUser(userId);
    }
}