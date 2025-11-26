package main.java.com.example.HU4.domain.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Venue {
    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private String description;
}