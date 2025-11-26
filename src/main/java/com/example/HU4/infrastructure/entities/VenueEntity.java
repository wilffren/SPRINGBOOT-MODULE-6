package com.example.HU4.infrastructure.entities;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "venues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private String description;
}