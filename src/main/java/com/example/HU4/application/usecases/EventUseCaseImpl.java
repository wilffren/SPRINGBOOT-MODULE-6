package com.example.HU4.application.usecases;

import com.example.HU4.domain.model.Event;
import com.example.HU4.domain.ports.in.EventUseCase;
import com.example.HU4.domain.ports.out.EventRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class EventUseCaseImpl implements EventUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    public EventUseCaseImpl(EventRepositoryPort eventRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
    }

    @Override
    @Transactional
    public Event createEvent(Event event) {
        // Aquí podrías agregar lógica de negocio adicional antes de guardar
        return eventRepositoryPort.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Event> getEventById(Long id) {
        return eventRepositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return eventRepositoryPort.findAll();
    }

    @Override
    @Transactional
    public Event updateEvent(Long id, Event event) {
        if (!eventRepositoryPort.existsById(id)) {
            throw new RuntimeException("Evento no encontrado con ID: " + id);
        }
        // Rebuild with the provided ID since Event is immutable (@Builder)
        Event eventWithId = Event.builder()
                .id(id)
                .name(event.getName())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .status(event.getStatus())
                .venueId(event.getVenueId())
                .venue(event.getVenue())
                .build();
        return eventRepositoryPort.save(eventWithId);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepositoryPort.existsById(id)) {
            throw new RuntimeException("Evento no encontrado con ID: " + id);
        }
        eventRepositoryPort.deleteById(id);
    }
}