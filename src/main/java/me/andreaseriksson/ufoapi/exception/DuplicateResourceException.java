package me.andreaseriksson.ufoapi.exception;

/**
 * Exception thrown when an attempt is made to create a resource that already exists.
 *
 * Used to signal duplicate entries, such as when registering a user with an existing username or email.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
