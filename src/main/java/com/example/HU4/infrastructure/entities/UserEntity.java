package com.example.HU4.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA - Usuario
 * TASK 1: Implementación de relaciones OneToMany con TaskEntity
 * Configuración de cascade, orphanRemoval y fetchType
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "tasks")
@ToString(exclude = "tasks")
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String username;
    
    @Column(nullable = false, unique = true, length = 150)
    private String email;
    
    @Column(name = "full_name", length = 200)
    private String fullName;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * TASK 1: Relación OneToMany con TaskEntity
     * - mappedBy: indica que TaskEntity es el owner de la relación
     * - cascade: propaga operaciones persist, merge, remove
     * - orphanRemoval: elimina tareas huérfanas automáticamente
     * - fetch: LAZY para evitar cargas innecesarias (optimización)
     */
    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<TaskEntity> tasks = new ArrayList<>();
    
    /**
     * Método helper para mantener sincronizada la relación bidireccional
     */
    public void addTask(TaskEntity task) {
        tasks.add(task);
        task.setUser(this);
    }
    
    /**
     * Método helper para remover tareas y mantener sincronización
     */
    public void removeTask(TaskEntity task) {
        tasks.remove(task);
        task.setUser(null);
    }
}