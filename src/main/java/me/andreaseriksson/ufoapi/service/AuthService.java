package me.andreaseriksson.ufoapi.service;

import me.andreaseriksson.ufoapi.dto.RegisterRequest;
import me.andreaseriksson.ufoapi.dto.RegisterResponse;
import me.andreaseriksson.ufoapi.entity.User;
import me.andreaseriksson.ufoapi.exception.DuplicateResourceException;
import me.andreaseriksson.ufoapi.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
