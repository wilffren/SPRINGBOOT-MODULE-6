package com.example.HU4.infrastructure.adapters.mappers;

import com.example.HU4.domain.model.Task;
import com.example.HU4.infrastructure.entities.TaskEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper entre TaskEntity (infraestructura) y Task (dominio)
 * Desacopla la capa de dominio de JPA
 */
@Component
public class TaskMapper {

    /**
     * Convierte de Entity a Domain
     */
    public Task toDomain(TaskEntity entity) {
        if (entity == null)
            return null;

        return Task.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .priority(entity.getPriority())
                .dueDate(entity.getDueDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .completedAt(entity.getCompletedAt())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .build();
    }

    /**
     * Convierte de Domain a Entity
     */
    public TaskEntity toEntity(Task domain) {
        if (domain == null)
            return null;

        return TaskEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .status(domain.getStatus())
                .priority(domain.getPriority())
                .dueDate(domain.getDueDate())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .completedAt(domain.getCompletedAt())
                .build();
    }

    /**
     * Actualiza una entidad existente con datos del dominio
     */
    public void updateEntity(TaskEntity entity, Task domain) {
        if (entity == null || domain == null)
            return;

        entity.setTitle(domain.getTitle());
        entity.setDescription(domain.getDescription());
        entity.setStatus(domain.getStatus());
        entity.setPriority(domain.getPriority());
        entity.setDueDate(domain.getDueDate());

        if (domain.getCompletedAt() != null) {
            entity.setCompletedAt(domain.getCompletedAt());
        }
    }
}