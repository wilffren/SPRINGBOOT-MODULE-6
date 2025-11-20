package com.example.HU4.domain.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo de dominio - User
 * Representa un usuario del sistema en la capa de dominio
 * Independiente de frameworks de persistencia
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Relación con tareas (no es una entidad JPA aquí)
    private List<Task> tasks;
    
    /**
     * Valida si el usuario está activo
     */
    public boolean isActive() {
        return active != null && active;
    }
    
    /**
     * Cuenta el número de tareas activas
     */
    public int getActiveTasksCount() {
        if (tasks == null) return 0;
        return (int) tasks.stream()
            .filter(task -> !"COMPLETED".equals(task.getStatus()))
            .count();
    }
}