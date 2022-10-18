package ru.mirea.megatracker.exceptions;

public class TokenRefreshNotFoundException extends RuntimeException {
    public TokenRefreshNotFoundException() {
        super("Unable to update auth: Refresh token was not found");
    }
}
