package me.andreaseriksson.webapi.repository;

import me.andreaseriksson.webapi.entity.Shape;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShapeRepository extends JpaRepository<Shape, Long> {

    Optional<Shape> findByName(String name);
}