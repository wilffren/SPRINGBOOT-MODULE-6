package main.java.com.example.HU4.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventStatus status;
    private Long venueId; // Referencia por ID para desacoplar
    // En Hexagonal puro a veces se trae el objeto Venue completo, 
    // pero por simplicidad de mapeo usaremos ID aquí o un objeto Venue anidado si la lógica lo requiere.
    private Venue venue; 
}