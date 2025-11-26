package main.java.com.example.HU4.infrastructure.adapters.out.persistence;

import com.example.HU4.domain.model.Event;
import com.example.HU4.domain.ports.out.EventRepositoryPort;
import com.example.HU4.infrastructure.entities.EventEntity;
import com.example.HU4.infrastructure.entities.VenueEntity;
import com.example.HU4.infrastructure.mappers.EventMapper;
import com.example.HU4.infrastructure.repositories.JpaEventRepository;
import com.example.HU4.infrastructure.repositories.JpaVenueRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EventPersistenceAdapter implements EventRepositoryPort {
    private final JpaEventRepository jpaEventRepository;
    private final JpaVenueRepository jpaVenueRepository;
    private final EventMapper eventMapper;

    public EventPersistenceAdapter(JpaEventRepository jpaEventRepository, JpaVenueRepository jpaVenueRepository, EventMapper eventMapper) {
        this.jpaEventRepository = jpaEventRepository;
        this.jpaVenueRepository = jpaVenueRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public Event save(Event event) {
        VenueEntity venue = jpaVenueRepository.findById(event.getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue not found"));
        EventEntity entity = eventMapper.toEntity(event, venue);
        return eventMapper.toDomain(jpaEventRepository.save(entity));
    }

    @Override
    public Optional<Event> findById(Long id) {
        return jpaEventRepository.findById(id).map(eventMapper::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return jpaEventRepository.findAll().stream().map(eventMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaEventRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaEventRepository.existsById(id);
    }
}