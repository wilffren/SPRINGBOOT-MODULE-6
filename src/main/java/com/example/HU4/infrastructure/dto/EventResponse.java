package com.example.HU4.infrastructure.dto;

import com.example.HU4.domain.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de response para Event
 * Incluye información del venue asociado
 */
public class EventResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventStatus status;
    private VenueResponse venue; // Incluimos el venue completo
    private List<String> categories; // Solo los nombres de las categorías
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public EventResponse() {
    }

    public EventResponse(Long id, String name, String description, LocalDateTime startDate,
            LocalDateTime endDate, EventStatus status, VenueResponse venue,
            List<String> categories, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.venue = venue;
        this.categories = categories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public VenueResponse getVenue() {
        return venue;
    }

    public void setVenue(VenueResponse venue) {
        this.venue = venue;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
