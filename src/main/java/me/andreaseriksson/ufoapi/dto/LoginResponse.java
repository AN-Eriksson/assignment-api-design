package me.andreaseriksson.ufoapi.dto;

public record LoginResponse(
        String token,
        String tokenType,
        Long expiresIn,
        Long userId,
        String username,
        String email
) {
}
