package ru.mirea.megatracker.controllerAdvices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.mirea.megatracker.exceptions.CoinNotFoundException;

@ControllerAdvice
public class CoinNotFoundControllerAdvice {

    @ResponseBody
    @ExceptionHandler(CoinNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String coinNotFoundHandler(CoinNotFoundException exception) {
        return exception.getMessage();
    }
}
