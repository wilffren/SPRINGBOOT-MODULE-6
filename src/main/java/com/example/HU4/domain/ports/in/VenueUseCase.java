package main.java.com.example.HU4.domain.ports.in;

import java.util.List;
import java.util.Optional;

public interface VenueUseCase {
    Venue createVenue(Venue venue);
    Optional<Venue> getVenueById(Long id);
    List<Venue> getAllVenues();
    Venue updateVenue(Long id, Venue venue);
    void deleteVenue(Long id);
}