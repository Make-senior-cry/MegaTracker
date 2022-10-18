package ru.mirea.megatracker.controllerAdvices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.mirea.megatracker.exceptions.CoinFetchFailedException;
import ru.mirea.megatracker.exceptions.CoinNotFoundException;

public class CoinExceptionsControllerAdvice {
    @ResponseBody
    @ExceptionHandler(CoinFetchFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle(CoinFetchFailedException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(CoinNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle(CoinNotFoundException exception) {
        return exception.getMessage();
    }
}
