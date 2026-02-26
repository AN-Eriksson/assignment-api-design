package me.andreaseriksson.webapi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sightings")
public class Sighting {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDateTime sightedAt;
    private Integer durationSeconds;
    private String durationText;

    @Column(columnDefinition = "TEXT")
    private String comments;

    private LocalDate datePosted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shape_id")
    private Shape shape;

    public Sighting() {}

    public Sighting(LocalDateTime sightedAt, Integer durationSeconds, String durationText,
                    String comments, LocalDate datePosted, Location location, Shape shape) {
        this.sightedAt = sightedAt;
        this.durationSeconds = durationSeconds;
        this.durationText = durationText;
        this.comments = comments;
        this.datePosted = datePosted;
        this.location = location;
        this.shape = shape;
    }
}