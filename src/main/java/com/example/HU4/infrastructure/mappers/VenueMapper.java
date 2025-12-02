package com.example.HU4.infrastructure.mappers;
import com.example.HU4.domain.model.Venue;
import com.example.HU4.infrastructure.entities.VenueEntity;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {
    public Venue toDomain(VenueEntity entity) {
        if (entity == null) return null;
        return Venue.builder().id(entity.getId()).name(entity.getName()).location(entity.getLocation()).capacity(entity.getCapacity()).description(entity.getDescription()).build();
    }
    public VenueEntity toEntity(Venue domain) {
        if (domain == null) return null;
        return new VenueEntity(domain.getId(), domain.getName(), domain.getLocation(), domain.getCapacity(), domain.getDescription());
    }
}