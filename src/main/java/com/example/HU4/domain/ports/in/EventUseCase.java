package main.java.com.example.HU4.domain.ports.in;

import com.example.HU4.domain.model.Event;
import java.util.List;
import java.util.Optional;

public interface EventUseCase {
    Event createEvent(Event event);
    Optional<Event> getEventById(Long id);
    List<Event> getAllEvents();
    Event updateEvent(Long id, Event event);
    void deleteEvent(Long id);
}