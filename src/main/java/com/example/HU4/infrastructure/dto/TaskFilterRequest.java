package com.example.HU4.infrastructure.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * TASK 2: DTO para filtros dinámicos de búsqueda
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskFilterRequest {
    
    private Long userId;
    private String status;
    private String priority;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDate;
}