package ru.mirea.megatracker.controllerAdvices;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.mirea.megatracker.exceptions.NoteNotFoundException;

@ControllerAdvice
public class NoteNotFoundControllerAdvice {

    @ResponseBody
    @ExceptionHandler(NoteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String noteNotFoundHandler(NoteNotFoundException exception) {
        return exception.getMessage();
    }
}
