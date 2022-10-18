package ru.mirea.megatracker.exceptions;

public class UnconfirmedPasswordException extends RuntimeException {

    public UnconfirmedPasswordException() {
        super("Passwords do not match");
    }
}
