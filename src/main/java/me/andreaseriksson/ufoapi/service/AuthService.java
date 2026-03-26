package me.andreaseriksson.ufoapi.service;

import me.andreaseriksson.ufoapi.dto.LoginRequest;
import me.andreaseriksson.ufoapi.dto.LoginResponse;
import me.andreaseriksson.ufoapi.dto.RegisterRequest;
import me.andreaseriksson.ufoapi.dto.RegisterResponse;
import me.andreaseriksson.ufoapi.entity.User;
import me.andreaseriksson.ufoapi.exception.DuplicateResourceException;
import me.andreaseriksson.ufoapi.exception.InvalidCredentialsException;
import me.andreaseriksson.ufoapi.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for handling user authentication and registration logic.
 *
 * Provides methods to register new users and authenticate existing users.
 * Handles password hashing, duplicate user checks, and JWT token generation.
 *
 * Throws DuplicateResourceException for duplicate usernames or emails,
 * and InvalidCredentialsException for failed login attempts.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public RegisterResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new DuplicateResourceException("Username already taken: " + req.username());
        }
        if (userRepository.existsByEmail(req.email())) {
            throw new DuplicateResourceException("Email already registered: " + req.email());
        }

        User user = new User(
                req.username(),
                req.email(),
                passwordEncoder.encode(req.password())
        );

        User saved = userRepository.save(user);

        return new RegisterResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail()
        );
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.login())
                .or(() -> userRepository.findByEmail(req.login()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username/email or password"));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getId(), user.getUsername());

        return new LoginResponse(
                token,
                "Bearer",
                jwtService.getExpirationMs(),
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
