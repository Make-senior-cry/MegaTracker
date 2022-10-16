package ru.mirea.megatracker.exceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Token has been expired");
    }
}
