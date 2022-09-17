package ru.mirea.megatracker.util;

public class UserNotAuthenticatedException extends RuntimeException{

    public UserNotAuthenticatedException(String message) {
        super(message);
    }

}
