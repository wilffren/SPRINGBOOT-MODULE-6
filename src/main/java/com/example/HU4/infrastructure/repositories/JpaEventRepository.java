package com.example.HU4.infrastructure.repositories;

import com.example.HU4.infrastructure.entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEventRepository extends JpaRepository<EventEntity, Long> {
    // Custom query methods can be added here if needed
}
