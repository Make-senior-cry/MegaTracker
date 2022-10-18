package ru.mirea.megatracker.controllerAdvices;

import com.fasterxml.jackson.core.JsonParseException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.jsonwebtoken.ExpiredJwtException;
import ru.mirea.megatracker.exceptions.EmailAlreadyExistsException;
import ru.mirea.megatracker.exceptions.TokenRefreshNotFoundException;
import ru.mirea.megatracker.exceptions.UnconfirmedPasswordException;
import ru.mirea.megatracker.exceptions.UserNotAuthenticatedException;
import ru.mirea.megatracker.exceptions.UserNotFoundException;

@ControllerAdvice
public class AuthExceptionsControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handle(ExpiredJwtException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(JsonParseException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserNotAuthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handle(UserNotAuthenticatedException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(TokenRefreshNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handle(TokenRefreshNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handle(UserNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UnconfirmedPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handle(UnconfirmedPasswordException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handle(EmailAlreadyExistsException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String handle(AuthenticationException exception) {
        return exception.getMessage();
    }
}
