package me.andreaseriksson.ufoapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "shapes")
public class Shape {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String name;

    public Shape() {}

    public Shape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}