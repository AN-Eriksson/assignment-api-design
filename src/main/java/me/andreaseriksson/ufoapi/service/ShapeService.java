package me.andreaseriksson.ufoapi.service;

import me.andreaseriksson.ufoapi.entity.Shape;
import me.andreaseriksson.ufoapi.repository.ShapeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShapeService {

    private final ShapeRepository shapeRepository;

    public ShapeService(ShapeRepository shapeRepository) {
        this.shapeRepository = shapeRepository;
    }

    public Shape save(Shape shape) {
        return shapeRepository.save(shape);
    }

    public List<Shape> findAll() {
        return shapeRepository.findAll();
    }

    public Optional<Shape> findByName(String name) {
        return shapeRepository.findByName(name);
    }
}