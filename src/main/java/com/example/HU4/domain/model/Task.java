package com.example.HU4.domain.model;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo de dominio - Task
 * Representa una tarea del sistema en la capa de dominio
 * Independiente de frameworks de persistencia
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    
    private Long id;
    private String title;
    private String description;
    private String status;  // PENDING, IN_PROGRESS, COMPLETED
    private String priority; // LOW, MEDIUM, HIGH
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    
    // Relación con usuario
    private User user;
    private Long userId;
    
    /**
     * Valida si la tarea está vencida
     */
    public boolean isOverdue() {
        if (dueDate == null || "COMPLETED".equals(status)) {
            return false;
        }
        return dueDate.isBefore(LocalDate.now());
    }
    
    /**
     * Marca la tarea como completada
     */
    public void complete() {
        this.status = "COMPLETED";
        this.completedAt = LocalDateTime.now();
    }
    
    /**
     * Valida si la tarea está completada
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
}