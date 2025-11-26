package com.example.HU4.infrastructure.adapters.out.persistence;

import com.example.HU4.domain.model.Venue;
import com.example.HU4.domain.ports.out.VenueRepositoryPort;
import com.example.HU4.infrastructure.entities.VenueEntity;
import com.example.HU4.infrastructure.mappers.VenueMapper;
import com.example.HU4.infrastructure.repositories.JpaVenueRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class VenuePersistenceAdapter implements VenueRepositoryPort {

    private final JpaVenueRepository jpaVenueRepository;
    private final VenueMapper venueMapper;

    public VenuePersistenceAdapter(JpaVenueRepository jpaVenueRepository, VenueMapper venueMapper) {
        this.jpaVenueRepository = jpaVenueRepository;
        this.venueMapper = venueMapper;
    }

    @Override
    public Venue save(Venue venue) {
        VenueEntity entity = venueMapper.toEntity(venue);
        VenueEntity savedEntity = jpaVenueRepository.save(entity);
        return venueMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return jpaVenueRepository.findById(id)
                .map(venueMapper::toDomain);
    }

    @Override
    public List<Venue> findAll() {
        return jpaVenueRepository.findAll().stream()
                .map(venueMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaVenueRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaVenueRepository.existsById(id);
    }
}
