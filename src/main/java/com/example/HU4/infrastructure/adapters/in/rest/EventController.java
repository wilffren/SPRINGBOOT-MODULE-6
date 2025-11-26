package main.java.com.example.HU4.infrastructure.adapters.in.rest;

import com.example.HU4.domain.model.Event;
import com.example.HU4.domain.ports.in.EventUseCase;
import com.example.HU4.infrastructure.dto.EventRequest;
import com.example.HU4.infrastructure.dto.EventResponse;
import com.example.HU4.infrastructure.validation.CreateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // HU5: Roles
import org.springframework.validation.annotation.Validated; // HU5: Validation Groups
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventUseCase eventUseCase;

    public EventController(EventUseCase eventUseCase) {
        this.eventUseCase = eventUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Seguridad HU5
    public ResponseEntity<EventResponse> createEvent(@Validated(CreateGroup.class) @RequestBody EventRequest request) {
        // Mapeo manual DTO -> Domain (o usar un DtoMapper)
        Event domain = Event.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus())
                .venueId(request.getVenueId())
                .build();
        
        Event created = eventUseCase.createEvent(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }
    
    // ... implementar GET, PUT, DELETE usando eventUseCase ...

    private EventResponse toResponse(Event e) {
        return EventResponse.builder()
            .id(e.getId())
            .name(e.getName())
            .status(e.getStatus())
            // ... resto de campos
            .build();
    }
}