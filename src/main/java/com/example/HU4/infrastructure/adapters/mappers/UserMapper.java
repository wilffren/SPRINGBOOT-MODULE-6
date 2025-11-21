package com.example.HU4.infrastructure.adapters.mappers;

import com.example.HU4.domain.model.User;
import com.example.HU4.infrastructure.entities.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper entre UserEntity (infraestructura) y User (dominio)
 */
@Component
public class UserMapper {
    
    public User toDomain(UserEntity entity) {
        if (entity == null) return null;
        
        return User.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .email(entity.getEmail())
            .fullName(entity.getFullName())
            .active(entity.getActive())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
    
    public UserEntity toEntity(User domain) {
        if (domain == null) return null;
        
        return UserEntity.builder()
            .id(domain.getId())
            .username(domain.getUsername())
            .email(domain.getEmail())
            .fullName(domain.getFullName())
            .active(domain.getActive())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .build();
    }
}