package com.eventcatalog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un evento en el catálogo.
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Esta clase solo representa la estructura de datos del evento.
 * - OCP (Open/Closed): Puede extenderse sin modificar el código existente.
 * - LSP (Liskov Substitution): Puede ser sustituida por cualquier subclase sin afectar el comportamiento.
 * 
 * @author
 */
@Entity
@Table(
    name = "events",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_event_name", columnNames = "name")
    },
    indexes = {
        @Index(name = "idx_event_category", columnList = "category"),
        @Index(name = "idx_event_date", columnList = "event_date"),
        @Index(name = "idx_event_city", columnList = "city")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventEntity {

    /** Identificador único del evento */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del evento (único) */
    @Column(nullable = false, unique = true, length = 150)
    private String name;

    /** Descripción detallada del evento */
    @Column(nullable = false, length = 1000)
    private String description;

    /** Categoría del evento (Concierto, Teatro, Deporte, etc.) */
    @Column(nullable = false, length = 50)
    private String category;

    /** Ciudad donde se realizará el evento */
    @Column(nullable = false, length = 50)
    private String city;

    /** Fecha y hora del evento */
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    /** Precio de la entrada */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** Número de entradas disponibles */
    @Column(name = "available_tickets", nullable = false)
    private Integer availableTickets;

    /** URL de la imagen del evento */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /**
     * Relación muchos-a-uno con el lugar del evento (VenueEntity)
     * Puede ser NULL → algunos eventos no tienen venue asignado.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
        name = "venue_id",
        nullable = true, // ✅ Permite NULL en la BD
        foreignKey = @ForeignKey(name = "fk_event_venue")
    )
    private VenueEntity venue;

    /** Estado del evento */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EventStatus status = EventStatus.ACTIVE;

    /** Fecha de creación automática */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Fecha de última actualización automática */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Enumeración para el estado del evento */
    public enum EventStatus {
        ACTIVE,      // Evento activo y disponible
        CANCELLED,   // Cancelado
        COMPLETED,   // Finalizado
        POSTPONED    // Pospuesto
    }

    /** toString seguro (evita lazy loading en venue) */
    @Override
    public String toString() {
        return "EventEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", city='" + city + '\'' +
                ", eventDate=" + eventDate +
                ", status=" + status +
                '}';
    }

    /** equals y hashCode basados en el nombre único */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventEntity)) return false;
        EventEntity that = (EventEntity) o;
        return name != null && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
