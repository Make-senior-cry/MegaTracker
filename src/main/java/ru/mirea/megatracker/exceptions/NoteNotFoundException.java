package ru.mirea.megatracker.exceptions;

public class NoteNotFoundException extends RuntimeException{
    public NoteNotFoundException(String ticker) {
        super(String.format("Note for ticker %s was not found", ticker));
    }
}
