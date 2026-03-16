package me.andreaseriksson.ufoapi.controller;

import jakarta.validation.Valid;
import me.andreaseriksson.ufoapi.dto.RegisterRequest;
import me.andreaseriksson.ufoapi.dto.RegisterResponse;
import me.andreaseriksson.ufoapi.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest req) {
        RegisterResponse response = authService.register(req);

        return ResponseEntity.status(201).body(response);
    }
}