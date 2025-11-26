package main.java.com.example.HU4.application.usecases;

import com.example.HU4.domain.model.Venue;
import com.example.HU4.domain.ports.in.VenueUseCase;
import com.example.HU4.domain.ports.out.VenueRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class VenueUseCaseImpl implements VenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    public VenueUseCaseImpl(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    @Transactional
    public Venue createVenue(Venue venue) {
        return venueRepositoryPort.save(venue);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venue> getVenueById(Long id) {
        return venueRepositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venue> getAllVenues() {
        return venueRepositoryPort.findAll();
    }

    @Override
    @Transactional
    public Venue updateVenue(Long id, Venue venue) {
        if (!venueRepositoryPort.existsById(id)) {
            throw new RuntimeException("Venue no encontrado con ID: " + id);
        }
        venue.setId(id);
        return venueRepositoryPort.save(venue);
    }

    @Override
    @Transactional
    public void deleteVenue(Long id) {
        if (!venueRepositoryPort.existsById(id)) {
            throw new RuntimeException("Venue no encontrado con ID: " + id);
        }
        venueRepositoryPort.deleteById(id);
    }
}