package me.andreaseriksson.ufoapi.service;

import me.andreaseriksson.ufoapi.entity.Shape;
import me.andreaseriksson.ufoapi.repository.ShapeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing Shape entities.
 *
 * Provides methods to save shapes, retrieve all shapes (optionally paged),
 * and look up shapes by name or ID.
 *
 * Acts as an intermediary between the ShapeRepository and higher-level application logic.
 */
@Service
public class ShapeService {

    private final ShapeRepository repository;

    public ShapeService(ShapeRepository repository) {
        this.repository = repository;
    }

    public Shape save(Shape shape) {
        return repository.save(shape);
    }

    public List<Shape> findAll() {
        return repository.findAll();
    }

    public Page<Shape> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Shape> findByName(String name) {
        return repository.findByName(name);
    }

    public Optional<Shape> findById(Long id) {
        return repository.findById(id);
    }

}