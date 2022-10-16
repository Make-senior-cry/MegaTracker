package ru.mirea.megatracker.controllerAdvices;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.mirea.megatracker.exceptions.UserNotFoundException;

@ControllerAdvice
public class UserNotFoundControllerAdvice {

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String linkNotFoundHandler(UserNotFoundException exception) {
        return exception.getMessage();
    }
}
