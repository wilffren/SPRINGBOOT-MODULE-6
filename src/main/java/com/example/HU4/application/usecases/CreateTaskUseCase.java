package com.example.HU4.application.usecases;

import com.example.HU4.domain.model.Task;
import com.example.HU4.domain.ports.in.CreateTaskPort;
import com.example.HU4.domain.ports.out.TaskRepositoryPort;
import com.example.HU4.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TASK 3: Caso de uso para crear tareas
 * Aplica @Transactional para garantizar atomicidad
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTaskUseCase implements CreateTaskPort {
    
    private final TaskRepositoryPort taskRepository;
    private final UserRepositoryPort userRepository;
    
    /**
     * TASK 3: Transacción de escritura
     * - readOnly = false (por defecto)
     * - propagation = REQUIRED (por defecto)
     * Si falla, hace rollback automático
     */
    @Override
    @Transactional
    public Task createTask(Task task) {
        log.info("Creando nueva tarea: {}", task.getTitle());
        
        // Validar que el usuario existe
        if (task.getUserId() != null) {
            userRepository.findById(task.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + task.getUserId()));
        }
        
        // Establecer valores por defecto si no vienen
        if (task.getStatus() == null) {
            task.setStatus("PENDING");
        }
        if (task.getPriority() == null) {
            task.setPriority("MEDIUM");
        }
        
        Task created = taskRepository.save(task);
        log.info("Tarea creada exitosamente con ID: {}", created.getId());
        
        return created;
    }
}