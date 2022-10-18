package ru.mirea.megatracker.controllerAdvices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.mirea.megatracker.exceptions.ValidationFailedException;

@ControllerAdvice
public class ValidationExceptionsControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ValidationFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(ValidationFailedException exception) {
        return exception.getMessage();
    }
}
