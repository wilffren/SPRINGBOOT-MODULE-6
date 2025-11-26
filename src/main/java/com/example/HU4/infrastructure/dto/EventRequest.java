package com.example.HU4.infrastructure.dto;

import com.example.HU4.domain.model.EventStatus;
import com.example.HU4.infrastructure.validation.CreateGroup;
import com.example.HU4.infrastructure.validation.DateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DateRange(groups = CreateGroup.class)
public class EventRequest {
    @NotBlank(groups = CreateGroup.class)
    private String name;

    private String description;

    @NotNull(groups = CreateGroup.class)
    private LocalDateTime startDate;

    @NotNull(groups = CreateGroup.class)
    private LocalDateTime endDate;

    private EventStatus status;

    @NotNull(groups = CreateGroup.class)
    private Long venueId;
}
