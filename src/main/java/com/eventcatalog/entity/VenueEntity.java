package com.eventcatalog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa un lugar/recinto donde se realizan eventos.
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Esta clase solo representa la estructura de datos del lugar
 * - OCP (Open/Closed): Puede extenderse sin modificar el código existente
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@Entity
@Table(name = "venues", indexes = {
    @Index(name = "idx_venue_name", columnList = "name"),
    @Index(name = "idx_venue_city", columnList = "city")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueEntity {

    /**
     * Identificador único del lugar
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del recinto/lugar
     * Debe ser único en el sistema
     */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /**
     * Dirección completa del lugar
     */
    @Column(nullable = false, length = 200)
    private String address;

    /**
     * Ciudad donde se encuentra el lugar
     */
    @Column(nullable = false, length = 50)
    private String city;

    /**
     * Capacidad máxima de personas
     */
    @Column(nullable = false)
    private Integer capacity;

    /**
     * Descripción adicional del lugar
     */
    @Column(length = 500)
    private String description;

    /**
     * Relación bidireccional uno-a-muchos con eventos
     * Un lugar puede tener múltiples eventos
     * 
     * mappedBy: Indica que EventEntity es el dueño de la relación
     * cascade: Las operaciones en cascada se aplicarán a los eventos
     * orphanRemoval: Si un evento se elimina de la lista, se borra de la BD
     */
    @OneToMany(
        mappedBy = "venue", 
        cascade = CascadeType.ALL, 
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<EventEntity> events = new ArrayList<>();

    /**
     * Fecha de creación del registro (automática)
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización del registro (automática)
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Método helper para agregar un evento al lugar
     * Mantiene la coherencia bidireccional de la relación
     * 
     * @param event Evento a agregar
     */
    public void addEvent(EventEntity event) {
        events.add(event);
        event.setVenue(this);
    }

    /**
     * Método helper para remover un evento del lugar
     * Mantiene la coherencia bidireccional de la relación
     * 
     * @param event Evento a remover
     */
    public void removeEvent(EventEntity event) {
        events.remove(event);
        event.setVenue(null);
    }

    /**
     * Método toString personalizado para evitar recursión infinita
     * debido a la relación bidireccional
     */
    @Override
    public String toString() {
        return "VenueEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}