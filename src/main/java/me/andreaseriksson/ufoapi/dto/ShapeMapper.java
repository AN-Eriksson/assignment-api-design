package me.andreaseriksson.ufoapi.dto;

import me.andreaseriksson.ufoapi.entity.Shape;

/**
 * Utility class for mapping Shape entities to ShapeResponse DTOs.
 *
 * Provides a static method to convert a Shape entity to its corresponding ShapeResponse.
 * This class is not intended to be instantiated.
 */
public final class ShapeMapper {

    private ShapeMapper() {
    }

    public static ShapeResponse toResponse(Shape shape) {
        return new ShapeResponse(
                shape.getId(),
                shape.getName()
        );
    }
}
