package com.example.HU4.infrastructure.repositories;

import com.example.HU4.infrastructure.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para UserEntity
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    
    Optional<UserEntity> findByUsername(String username);
    
    Optional<UserEntity> findByEmail(String email);
    
    @Query("SELECT u FROM UserEntity u WHERE u.username = :username AND u.active = true")
    Optional<UserEntity> findActiveByUsername(@Param("username") String username);
}