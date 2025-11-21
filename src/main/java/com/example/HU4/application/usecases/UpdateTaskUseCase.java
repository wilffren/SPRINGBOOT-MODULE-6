package com.example.HU4.application.usecases;

import com.example.HU4.domain.model.Task;
import com.example.HU4.domain.ports.in.UpdateTaskPort;
import com.example.HU4.domain.ports.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * TASK 3: Caso de uso para actualizar tareas
 * Aplica transaccionalidad con diferentes configuraciones
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateTaskUseCase implements UpdateTaskPort {
    
    private final TaskRepositoryPort taskRepository;
    
    /**
     * TASK 3: Transacción de escritura con propagación REQUIRED
     * Si hay una transacción activa, la usa; sino crea una nueva
     */
    @Override
    @Transactional
    public Task updateTask(Long id, Task task) {
        log.info("Actualizando tarea ID: {}", id);
        
        Task existing = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + id));
        
        // Actualizar campos
        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setStatus(task.getStatus());
        existing.setPriority(task.getPriority());
        existing.setDueDate(task.getDueDate());
        
        if (task.getUserId() != null) {
            existing.setUserId(task.getUserId());
        }
        
        Task updated = taskRepository.save(existing);
        log.info("Tarea actualizada exitosamente: {}", id);
        
        return updated;
    }
    
    /**
     * TASK 3: Marca una tarea como completada
     * Operación atómica dentro de una transacción
     */
    @Override
    @Transactional
    public Task completeTask(Long id) {
        log.info("Marcando tarea como completada: {}", id);
        
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + id));
        
        task.setStatus("COMPLETED");
        task.setCompletedAt(LocalDateTime.now());
        
        Task completed = taskRepository.save(task);
        log.info("Tarea completada: {}", id);
        
        return completed;
    }
    
    /**
     * TASK 3: Elimina una tarea
     * Si hay error, hace rollback automático
     */
    @Override
    @Transactional
    public void deleteTask(Long id) {
        log.info("Eliminando tarea ID: {}", id);
        
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Tarea no encontrada con ID: " + id);
        }
        
        taskRepository.deleteById(id);
        log.info("Tarea eliminada exitosamente: {}", id);
    }
}