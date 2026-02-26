package me.andreaseriksson.webapi.entity;

import jakarta.persistence.*;

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
}