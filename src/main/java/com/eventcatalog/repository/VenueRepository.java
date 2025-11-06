package com.eventcatalog.repository;

import com.eventcatalog.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface VenueRepository extends JpaRepository<VenueEntity, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<VenueEntity> findByCity(String city);

    @Query("SELECT COUNT(e) FROM EventEntity e WHERE e.venue.id = :venueId")
    Integer countEventsByVenueId(@Param("venueId") Long venueId);
}
