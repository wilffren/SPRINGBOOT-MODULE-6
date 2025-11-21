package com.example.HU4.infrastructure.adapters;

import com.example.HU4.domain.model.Task;
import com.example.HU4.domain.ports.out.TaskRepositoryPort;
import com.example.HU4.infrastructure.adapters.mappers.TaskMapper;
import com.example.HU4.infrastructure.entities.TaskEntity;
import com.example.HU4.infrastructure.entities.UserEntity;
import com.example.HU4.infrastructure.repositories.TaskJpaRepository;
import com.example.HU4.infrastructure.repositories.UserJpaRepository;
import com.example.HU4.infrastructure.repositories.specifications.TaskSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador del repositorio de tareas
 * Implementa el puerto de salida TaskRepositoryPort
 * TASK 2: Usa Specifications para consultas dinámicas
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepositoryPort {
    
    private final TaskJpaRepository taskRepository;
    private final UserJpaRepository userRepository;
    private final TaskMapper taskMapper;
    
    @Override
    public Task save(Task task) {
        TaskEntity entity = taskMapper.toEntity(task);
        
        // Si tiene userId, asignar la relación
        if (task.getUserId() != null) {
            UserEntity user = userRepository.findById(task.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            entity.setUser(user);
        }
        
        TaskEntity saved = taskRepository.save(entity);
        log.debug("Tarea guardada: {}", saved.getId());
        
        return taskMapper.toDomain(saved);
    }
    
    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id)
            .map(taskMapper::toDomain);
    }
    
    @Override
    public List<Task> findAll() {
        return taskRepository.findAll().stream()
            .map(taskMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Task> findByUserId(Long userId) {
        return taskRepository.findByUserId(userId).stream()
            .map(taskMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Task> findByStatus(String status) {
        return taskRepository.findByStatus(status).stream()
            .map(taskMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Task> findByPriority(String priority) {
        return taskRepository.findByPriority(priority).stream()
            .map(taskMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    /**
     * TASK 2: Usa Specifications para filtros dinámicos
     */
    @Override
    public List<Task> findByFilters(Long userId, String status, String priority, LocalDate dueDate) {
        Specification<TaskEntity> spec = TaskSpecifications.withFilters(
            userId, status, priority, dueDate
        );
        
        log.debug("Ejecutando consulta con filtros: userId={}, status={}, priority={}, dueDate={}", 
            userId, status, priority, dueDate);
        
        return taskRepository.findAll(spec).stream()
            .map(taskMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    /**
     * TASK 2: Resuelve N+1 con @EntityGraph
     */
    @Override
    public List<Task> findByUserIdWithUser(Long userId) {
        log.debug("Ejecutando consulta optimizada para userId: {}", userId);
        
        return taskRepository.findByUserIdWithUser(userId).stream()
            .map(taskMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
        log.debug("Tarea eliminada: {}", id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }
}