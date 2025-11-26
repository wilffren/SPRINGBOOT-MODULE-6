package main.java.com.example.HU4.infrastructure.mappers;
import com.example.HU4.domain.model.Event;
import com.example.HU4.domain.model.Venue;
import com.example.HU4.infrastructure.entities.EventEntity;
import com.example.HU4.infrastructure.entities.VenueEntity;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public Event toDomain(EventEntity entity) {
        if (entity == null) return null;
        Venue venueDomain = null;
        if(entity.getVenue() != null) {
             venueDomain = new Venue(entity.getVenue().getId(), entity.getVenue().getName(), entity.getVenue().getLocation(), entity.getVenue().getCapacity(), entity.getVenue().getDescription());
        }
        return Event.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .startDate(entity.getStartDate())
            .endDate(entity.getEndDate())
            .status(entity.getStatus())
            .venueId(entity.getVenue() != null ? entity.getVenue().getId() : null)
            .venue(venueDomain)
            .build();
    }

    public EventEntity toEntity(Event domain, VenueEntity venueEntity) {
        EventEntity entity = new EventEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setStatus(domain.getStatus());
        entity.setVenue(venueEntity);
        return entity;
    }
}