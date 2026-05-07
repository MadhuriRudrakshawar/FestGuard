package com.tus.festguard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "festival_area")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FestivalArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Area name is required")
    private String name;

    private String description;

    @Column(name = "area_type")
    private String areaType;
}
