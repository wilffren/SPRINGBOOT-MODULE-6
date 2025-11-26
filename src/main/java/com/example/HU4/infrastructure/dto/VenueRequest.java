package com.example.HU4.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String location;

    @NotNull
    @Positive
    private Integer capacity;

    private String description;
}
