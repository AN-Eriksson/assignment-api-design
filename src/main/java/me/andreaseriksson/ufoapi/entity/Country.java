package me.andreaseriksson.ufoapi.entity;

import jakarta.persistence.*;

/**
 * JPA entity representing a country.
 *
 * Contains a unique ID and a country code.
 * Used to associate sightings and locations with a specific country.
 */
@Entity
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String code;

    public Country() {
    }

    public Country(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}