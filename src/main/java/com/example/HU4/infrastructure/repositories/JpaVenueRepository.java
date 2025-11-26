package com.example.HU4.infrastructure.repositories;

import com.example.HU4.infrastructure.entities.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaVenueRepository extends JpaRepository<VenueEntity, Long> {
    // Custom query methods can be added here if needed
}
