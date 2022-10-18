package ru.mirea.megatracker.exceptions;

public class ValidationFailedException extends RuntimeException {
    public ValidationFailedException(String message) {
        super(message);
    }

    public ValidationFailedException() {
        super("Validation failed");
    }
}
