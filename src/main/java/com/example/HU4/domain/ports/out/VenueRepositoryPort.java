package com.example.HU4.domain.ports.out;
import com.example.HU4.domain.model.Venue;
import java.util.List;
import java.util.Optional;

public interface VenueRepositoryPort {
    Venue save(Venue venue);
    Optional<Venue> findById(Long id);
    List<Venue> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}