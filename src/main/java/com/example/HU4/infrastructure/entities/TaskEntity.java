package com.example.HU4.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad JPA - Tarea
 * TASK 1: Implementación de relación ManyToOne con UserEntity
 * Configuración de fetchType y cascade
 */
@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "user")
@ToString(exclude = "user")
public class TaskEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, length = 50)
    private String status = "PENDING";
    
    @Column(nullable = false, length = 50)
    private String priority = "MEDIUM";
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    /**
     * TASK 1: Relación ManyToOne con UserEntity
     * - fetch: LAZY para evitar carga automática del usuario (optimización)
     * - optional: false indica que toda tarea debe tener un usuario
     * - JoinColumn: define la FK en la tabla tasks
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_task_user"))
    private UserEntity user;
    
    /**
     * Método para marcar como completada
     */
    public void complete() {
        this.status = "COMPLETED";
        this.completedAt = LocalDateTime.now();
    }
}