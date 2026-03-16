package me.andreaseriksson.ufoapi.dto;

import me.andreaseriksson.ufoapi.entity.Shape;

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
