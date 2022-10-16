package ru.mirea.megatracker.controllerAdvices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.mirea.megatracker.exceptions.TokenExpiredException;

@ControllerAdvice
public class TokenExpiredControllerAdvice {

    @ResponseBody
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String tokenExpiredHandler(TokenExpiredException exception) {
        return exception.getMessage();
    }
}
