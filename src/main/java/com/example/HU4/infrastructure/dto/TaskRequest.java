package com.example.HU4.infrastructure.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO para recibir datos de creación/actualización de tareas
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 200, message = "El título debe tener entre 3 y 200 caracteres")
    private String title;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;
    
    @Pattern(
        regexp = "PENDING|IN_PROGRESS|COMPLETED", 
        message = "Estado inválido. Valores permitidos: PENDING, IN_PROGRESS, COMPLETED"
    )
    private String status;
    
    @Pattern(
        regexp = "LOW|MEDIUM|HIGH", 
        message = "Prioridad inválida. Valores permitidos: LOW, MEDIUM, HIGH"
    )
    private String priority;
    
    @Future(message = "La fecha de vencimiento debe ser futura")
    private LocalDate dueDate;
    
    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser positivo")
    private Long userId;
}