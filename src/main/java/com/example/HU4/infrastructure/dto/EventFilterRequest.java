package com.example.HU4.infrastructure.dto;

import com.example.HU4.domain.model.EventStatus;

import java.time.LocalDateTime;

/**
 * DTO para filtrar eventos dinámicamente
 * Todos los campos son opcionales
 */
public class EventFilterRequest {
    private Long venueId;
    private EventStatus status;
    private LocalDateTime startDateFrom;
    private LocalDateTime startDateTo;
    private LocalDateTime endDateFrom;
    private LocalDateTime endDateTo;
    private String categoryName;
    private String nameContains;

    // Constructor
    public EventFilterRequest() {
    }

    // Getters and Setters
    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartDateFrom() {
        return startDateFrom;
    }

    public void setStartDateFrom(LocalDateTime startDateFrom) {
        this.startDateFrom = startDateFrom;
    }

    public LocalDateTime getStartDateTo() {
        return startDateTo;
    }

    public void setStartDateTo(LocalDateTime startDateTo) {
        this.startDateTo = startDateTo;
    }

    public LocalDateTime getEndDateFrom() {
        return endDateFrom;
    }

    public void setEndDateFrom(LocalDateTime endDateFrom) {
        this.endDateFrom = endDateFrom;
    }

    public LocalDateTime getEndDateTo() {
        return endDateTo;
    }

    public void setEndDateTo(LocalDateTime endDateTo) {
        this.endDateTo = endDateTo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getNameContains() {
        return nameContains;
    }

    public void setNameContains(String nameContains) {
        this.nameContains = nameContains;
    }
}
