package com.example.HU4.infrastructure.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para respuesta de tareas
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private Long userId;
    private String username;
    private boolean overdue;
}