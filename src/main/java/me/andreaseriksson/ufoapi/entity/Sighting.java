package me.andreaseriksson.ufoapi.entity;

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

    public long getId() {
        return id;
    }

    public LocalDateTime getSightedAt() {
        return sightedAt;
    }

    public void setSightedAt(LocalDateTime sightedAt) {
        this.sightedAt = sightedAt;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDate getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDate datePosted) {
        this.datePosted = datePosted;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

}