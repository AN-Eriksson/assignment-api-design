package me.andreaseriksson.ufoapi.dto;

public record RegisterResponse(
        Long id,
        String username,
        String email
) {
}
